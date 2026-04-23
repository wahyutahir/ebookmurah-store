package com.ebookmurah.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendPurchaseEmail(String toEmail, String fullName, String ebookTitle, String downloadLink, String password) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Akses Ebook Anda: " + ebookTitle + " - EbookMurah Store");
            helper.setFrom("noreply@ebookmurah.store");

            Context context = new Context();
            context.setVariable("fullName", fullName);
            context.setVariable("ebookTitle", ebookTitle);
            context.setVariable("downloadLink", downloadLink);
            context.setVariable("password", password);
            context.setVariable("loginUrl", "https://ebookmurah.store/login");

            String htmlContent = templateEngine.process("email/purchase-email", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Purchase email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send purchase email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    public void sendWelcomeEmail(String toEmail, String fullName, String password) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Selamat Datang di EbookMurah Store!");
            helper.setFrom("noreply@ebookmurah.store");

            Context context = new Context();
            context.setVariable("fullName", fullName);
            context.setVariable("password", password);
            context.setVariable("loginUrl", "https://ebookmurah.store/login");

            String htmlContent = templateEngine.process("email/welcome-email", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Welcome email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send welcome email to: {}", toEmail, e);
        }
    }
}
