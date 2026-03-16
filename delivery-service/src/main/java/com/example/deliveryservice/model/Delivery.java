package com.example.deliveryservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street",   column = @Column(name = "address_street")),
        @AttributeOverride(name = "city",     column = @Column(name = "address_city")),
        @AttributeOverride(name = "zipCode",  column = @Column(name = "address_zip_code"))
    })
    private Address address;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "courierId",    column = @Column(name = "courier_id")),
        @AttributeOverride(name = "courierName",  column = @Column(name = "courier_name")),
        @AttributeOverride(name = "courierPhone", column = @Column(name = "courier_phone"))
    })
    private CourierInfo courierInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = DeliveryStatus.PENDING;
        }
    }

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void assignCourier(CourierInfo courier) {
        this.courierInfo = courier;
        this.status = DeliveryStatus.ASSIGNED;
    }

    public void updateStatus(DeliveryStatus newStatus) {
        this.status = newStatus;
    }
}
