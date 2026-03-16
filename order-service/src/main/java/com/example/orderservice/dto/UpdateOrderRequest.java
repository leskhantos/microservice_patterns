package com.example.orderservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateOrderRequest {
    private String street;
    private String city;
    private String postalCode;
    private String country;
}
