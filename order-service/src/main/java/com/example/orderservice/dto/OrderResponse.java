package com.example.orderservice.dto;

import com.example.orderservice.domain.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {

    private Long id;
    private String orderNumber;
    private String customerId;

    private String street;
    private String city;
    private String postalCode;
    private String country;

    private OrderStatus status;
    private List<OrderItemDto> items;

    private BigDecimal totalAmount;
    private String totalCurrency;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
