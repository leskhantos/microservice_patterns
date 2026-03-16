package com.example.orderservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private Long id;
    private String productId;
    private String productName;
    private int quantity;
    private BigDecimal unitPriceAmount;
    private String unitPriceCurrency;
    private BigDecimal totalPriceAmount;
}
