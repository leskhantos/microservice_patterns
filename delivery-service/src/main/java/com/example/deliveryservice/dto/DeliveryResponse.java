package com.example.deliveryservice.dto;

import com.example.deliveryservice.model.DeliveryStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DeliveryResponse {

    private Long id;
    private Long orderId;
    private String street;
    private String city;
    private String zipCode;
    private Long courierId;
    private String courierName;
    private String courierPhone;
    private DeliveryStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
