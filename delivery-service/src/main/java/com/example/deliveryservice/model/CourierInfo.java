package com.example.deliveryservice.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourierInfo {

    private Long courierId;
    private String courierName;
    private String courierPhone;
}
