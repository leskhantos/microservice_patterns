package com.example.deliveryservice.repository;

import com.example.deliveryservice.model.Delivery;
import com.example.deliveryservice.model.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    List<Delivery> findByOrderId(Long orderId);

    List<Delivery> findByStatus(DeliveryStatus status);

    List<Delivery> findByCourierInfoCourierId(Long courierId);
}
