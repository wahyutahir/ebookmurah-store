package com.ebookmurah.service;

import com.ebookmurah.config.MidtransConfig;
import com.ebookmurah.entity.Ebook;
import com.ebookmurah.entity.Transaction;
import com.ebookmurah.entity.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MidtransService {

    private final MidtransConfig midtransConfig;
    private final TransactionService transactionService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String createPaymentTransaction(User user, Ebook ebook, String paymentMethod) {
        try {
            Transaction transaction = transactionService.createTransaction(user, ebook, ebook.getPrice(), paymentMethod);

            Map<String, Object> transactionDetails = new HashMap<>();
            transactionDetails.put("order_id", transaction.getOrderId());
            transactionDetails.put("gross_amount", ebook.getPrice());

            Map<String, Object> customerDetails = new HashMap<>();
            customerDetails.put("first_name", user.getFullName());
            customerDetails.put("email", user.getEmail());
            customerDetails.put("phone", user.getPhoneNumber());

            Map<String, Object> itemDetails = new HashMap<>();
            itemDetails.put("id", ebook.getId());
            itemDetails.put("price", ebook.getPrice());
            itemDetails.put("quantity", 1);
            itemDetails.put("name", ebook.getTitle());

            Map<String, Object> payload = new HashMap<>();
            payload.put("transaction_details", transactionDetails);
            payload.put("customer_details", customerDetails);
            payload.put("item_details", new Object[]{itemDetails});
            payload.put("enabled_payments", getEnabledPayments(paymentMethod));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Basic " + getEncodedAuth());

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    midtransConfig.getApiUrl() + "/transactions",
                    request,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK) {
                JsonNode root = objectMapper.readTree(response.getBody());
                return root.path("redirect_url").asText();
            } else {
                throw new RuntimeException("Failed to create Midtrans transaction");
            }
        } catch (Exception e) {
            log.error("Error creating Midtrans transaction", e);
            throw new RuntimeException("Failed to create payment transaction: " + e.getMessage());
        }
    }

    public boolean handlePaymentNotification(String jsonPayload) {
        try {
            JsonNode root = objectMapper.readTree(jsonPayload);
            String orderId = root.path("order_id").asText();
            String transactionStatus = root.path("transaction_status").asText();
            String fraudStatus = root.path("fraud_status").asText();
            String paymentType = root.path("payment_type").asText();
            String transactionId = root.path("transaction_id").asText();

            String bank = root.has("bank") ? root.path("bank").asText() : null;
            String vaNumber = root.has("va_numbers") ? 
                    root.path("va_numbers").get(0).path("va_number").asText() : null;

            String status = "PENDING";
            if ("capture".equals(transactionStatus)) {
                if ("accept".equals(fraudStatus)) {
                    status = "SUCCESS";
                }
            } else if ("settlement".equals(transactionStatus)) {
                status = "SUCCESS";
            } else if ("cancel".equals(transactionStatus) || "deny".equals(transactionStatus) || "expire".equals(transactionStatus)) {
                status = "FAILED";
            } else if ("pending".equals(transactionStatus)) {
                status = "PENDING";
            }

            transactionService.updateTransactionStatus(
                    orderId,
                    status,
                    transactionId,
                    transactionStatus,
                    paymentType,
                    bank,
                    vaNumber
            );

            return "SUCCESS".equals(status);
        } catch (Exception e) {
            log.error("Error handling payment notification", e);
            return false;
        }
    }

    private String[] getEnabledPayments(String paymentMethod) {
        return switch (paymentMethod.toLowerCase()) {
            case "qris" -> new String[]{"qris"};
            case "va" -> new String[]{"bca_va", "bni_va", "bri_va", "mandiri_va", "permata_va", "cimb_va"};
            case "ewallet" -> new String[]{"gopay", "ovo", "dana", "shopeepay", "linkaja"};
            default -> new String[]{"qris", "bca_va", "bni_va", "bri_va", "gopay", "ovo", "dana"};
        };
    }

    private String getEncodedAuth() {
        String auth = midtransConfig.getServerKey() + ":";
        return Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
    }
}
