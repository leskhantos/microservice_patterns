package com.example.deliveryservice.dto;

import com.example.deliveryservice.model.DeliveryStatus;
import lombok.Data;

@Data
public class DeliveryRequest {

    private Long orderId;
    private String street;
    private String city;
    private String zipCode;
    private Long courierId;
    private String courierName;
    private String courierPhone;
    private DeliveryStatus status;
}
