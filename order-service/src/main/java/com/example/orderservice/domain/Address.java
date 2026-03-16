package com.example.orderservice.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Value Object: shipping address, equality by value.
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String street;
    private String city;
    private String postalCode;
    private String country;

    @Override
    public String toString() {
        return street + ", " + city + " " + postalCode + ", " + country;
    }
}
