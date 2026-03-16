package com.example.paymentservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdatePaymentRequest {

    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
}
