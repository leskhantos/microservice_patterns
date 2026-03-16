package com.example.paymentservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePaymentRequest {

    private String orderId;
    private String customerId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
}
