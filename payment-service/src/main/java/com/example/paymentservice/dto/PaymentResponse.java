package com.example.paymentservice.dto;

import com.example.paymentservice.domain.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponse {

    private Long id;
    private String paymentNumber;
    private String orderId;
    private String customerId;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private String paymentMethod;
    private String failureReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
