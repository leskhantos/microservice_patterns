package com.example.orderservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateOrderRequest {

    private String customerId;

    private String street;
    private String city;
    private String postalCode;
    private String country;

    private List<CreateOrderItemRequest> items;

    @Data
    public static class CreateOrderItemRequest {
        private String productId;
        private String productName;
        private int quantity;
        private BigDecimal unitPriceAmount;
        private String unitPriceCurrency;
    }
}
