package com.ebookmurah.controller;

import com.ebookmurah.entity.Ebook;
import com.ebookmurah.entity.Transaction;
import com.ebookmurah.entity.User;
import com.ebookmurah.service.EmailService;
import com.ebookmurah.service.EbookService;
import com.ebookmurah.service.MidtransService;
import com.ebookmurah.service.TransactionService;
import com.ebookmurah.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final MidtransService midtransService;
    private final TransactionService transactionService;
    private final EbookService ebookService;
    private final UserService userService;
    private final EmailService emailService;

    @PostMapping("/payment/create")
    public String createPayment(@RequestParam Long ebookId,
                                @RequestParam String paymentMethod,
                                @AuthenticationPrincipal User user,
                                Model model) {
        try {
            Ebook ebook = ebookService.getById(ebookId)
                    .orElseThrow(() -> new RuntimeException("Ebook not found"));

            String redirectUrl = midtransService.createPaymentTransaction(user, ebook, paymentMethod);
            return "redirect:" + redirectUrl;
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/payment/guest-order")
    public String createGuestOrder(@RequestParam Long ebookId,
                                   @RequestParam String fullName,
                                   @RequestParam String email,
                                   @RequestParam String whatsapp,
                                   Model model) {
        try {
            Ebook ebook = ebookService.getById(ebookId)
                    .orElseThrow(() -> new RuntimeException("Ebook not found"));

            // Create temporary user for guest order
            String generatedPassword = UUID.randomUUID().toString().substring(0, 12);
            User savedUser = userService.createUserFromPayment(email, fullName, whatsapp, generatedPassword);
            
            // Create transaction
            Transaction transaction = transactionService.createTransaction(savedUser, ebook, ebook.getPrice(), "GUEST_ORDER");
            
            // Redirect to payment page with guest payment options
            String redirectUrl = midtransService.createPaymentTransaction(savedUser, ebook, "qris");
            return "redirect:" + redirectUrl;
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/api/payment/notification")
    public ResponseEntity<?> handlePaymentNotification(@RequestBody String payload) {
        try {
            boolean isSuccess = midtransService.handlePaymentNotification(payload);
            
            if (isSuccess) {
                // Extract order_id from payload to get transaction details
                com.fasterxml.jackson.databind.JsonNode root = new com.fasterxml.jackson.databind.ObjectMapper().readTree(payload);
                String orderId = root.path("order_id").asText();
                
                Transaction transaction = transactionService.getByOrderId(orderId).orElse(null);
                if (transaction != null) {
                    // Send email with download link
                    String generatedPassword = generatePassword();
                    userService.updateLastLogin(transaction.getUser().getId());
                    
                    emailService.sendPurchaseEmail(
                            transaction.getUser().getEmail(),
                            transaction.getUser().getFullName(),
                            transaction.getEbook().getTitle(),
                            transaction.getEbook().getPdfDownloadLink(),
                            generatedPassword
                    );
                }
            }
            
            Map<String, String> response = new HashMap<>();
            response.put("status", isSuccess ? "success" : "failed");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/payment/success")
    public String paymentSuccess(@RequestParam String order_id, Model model) {
        Transaction transaction = transactionService.getByOrderId(order_id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        
        model.addAttribute("transaction", transaction);
        model.addAttribute("ebook", transaction.getEbook());
        return "payment/success";
    }

    @GetMapping("/payment/pending")
    public String paymentPending(@RequestParam String order_id, Model model) {
        Transaction transaction = transactionService.getByOrderId(order_id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        
        model.addAttribute("transaction", transaction);
        return "payment/pending";
    }

    @GetMapping("/payment/error")
    public String paymentError(@RequestParam String order_id, Model model) {
        Transaction transaction = transactionService.getByOrderId(order_id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        
        model.addAttribute("transaction", transaction);
        return "payment/error";
    }

    private String generatePassword() {
        return UUID.randomUUID().toString().substring(0, 12);
    }
}
