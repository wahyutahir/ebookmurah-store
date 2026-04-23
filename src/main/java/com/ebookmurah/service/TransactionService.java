package com.ebookmurah.service;

import com.ebookmurah.entity.Transaction;
import com.ebookmurah.entity.User;
import com.ebookmurah.entity.Ebook;
import com.ebookmurah.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Transactional
    public Transaction createTransaction(User user, Ebook ebook, BigDecimal amount, String paymentMethod) {
        String orderId = "ORDER-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Transaction transaction = Transaction.builder()
                .orderId(orderId)
                .user(user)
                .ebook(ebook)
                .amount(amount)
                .paymentMethod(paymentMethod)
                .status("PENDING")
                .transactionTime(LocalDateTime.now())
                .expiryTime(LocalDateTime.now().plusHours(24))
                .build();

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction updateTransactionStatus(String orderId, String status, 
                                               String midtransTransactionId, 
                                               String midtransTransactionStatus,
                                               String paymentType,
                                               String bank,
                                               String vaNumber) {
        Transaction transaction = transactionRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setStatus(status);
        transaction.setMidtransTransactionId(midtransTransactionId);
        transaction.setMidtransTransactionStatus(midtransTransactionStatus);
        transaction.setPaymentType(paymentType);
        transaction.setBank(bank);
        transaction.setVaNumber(vaNumber);

        if ("SUCCESS".equals(status)) {
            transaction.setSettlementTime(LocalDateTime.now());
        }

        return transactionRepository.save(transaction);
    }

    public Optional<Transaction> getByOrderId(String orderId) {
        return transactionRepository.findByOrderId(orderId);
    }

    public List<Transaction> getUserTransactions(Long userId) {
        return transactionRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Optional<Transaction> getByMidtransTransactionId(String transactionId) {
        return transactionRepository.findByMidtransTransactionId(transactionId);
    }
}
