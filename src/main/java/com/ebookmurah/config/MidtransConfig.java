package com.ebookmurah.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "midtrans")
public class MidtransConfig {
    private String serverKey;
    private String clientKey;
    private String apiUrl;
    private String paymentNotificationUrl;
    private String paymentFinishUrl;
    private String paymentUnfinishUrl;
    private String paymentErrorUrl;
}
