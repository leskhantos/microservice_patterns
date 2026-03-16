package com.example.deliveryservice.service;

import com.example.deliveryservice.dto.DeliveryRequest;
import com.example.deliveryservice.dto.DeliveryResponse;
import com.example.deliveryservice.exception.DeliveryNotFoundException;
import com.example.deliveryservice.model.Address;
import com.example.deliveryservice.model.CourierInfo;
import com.example.deliveryservice.model.Delivery;
import com.example.deliveryservice.model.DeliveryStatus;
import com.example.deliveryservice.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public DeliveryResponse create(DeliveryRequest request) {
        Delivery delivery = Delivery.builder()
                .orderId(request.getOrderId())
                .address(new Address(request.getStreet(), request.getCity(), request.getZipCode()))
                .courierInfo(new CourierInfo(request.getCourierId(), request.getCourierName(), request.getCourierPhone()))
                .status(request.getStatus() != null ? request.getStatus() : DeliveryStatus.PENDING)
                .build();
        return toResponse(deliveryRepository.save(delivery));
    }

    @Transactional(readOnly = true)
    public List<DeliveryResponse> getAll() {
        return deliveryRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DeliveryResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<DeliveryResponse> getByOrderId(Long orderId) {
        return deliveryRepository.findByOrderId(orderId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DeliveryResponse> getByStatus(DeliveryStatus status) {
        return deliveryRepository.findByStatus(status).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public DeliveryResponse update(Long id, DeliveryRequest request) {
        Delivery delivery = findOrThrow(id);
        delivery.setOrderId(request.getOrderId());
        delivery.setAddress(new Address(request.getStreet(), request.getCity(), request.getZipCode()));
        delivery.setCourierInfo(new CourierInfo(request.getCourierId(), request.getCourierName(), request.getCourierPhone()));
        if (request.getStatus() != null) {
            delivery.updateStatus(request.getStatus());
        }
        return toResponse(deliveryRepository.save(delivery));
    }

    public DeliveryResponse updateStatus(Long id, DeliveryStatus status) {
        Delivery delivery = findOrThrow(id);
        delivery.updateStatus(status);
        return toResponse(deliveryRepository.save(delivery));
    }

    public void delete(Long id) {
        findOrThrow(id);
        deliveryRepository.deleteById(id);
    }

    private Delivery findOrThrow(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery not found with id: " + id));
    }

    private DeliveryResponse toResponse(Delivery d) {
        Address addr = d.getAddress();
        CourierInfo ci = d.getCourierInfo();
        return DeliveryResponse.builder()
                .id(d.getId())
                .orderId(d.getOrderId())
                .street(addr != null ? addr.getStreet() : null)
                .city(addr != null ? addr.getCity() : null)
                .zipCode(addr != null ? addr.getZipCode() : null)
                .courierId(ci != null ? ci.getCourierId() : null)
                .courierName(ci != null ? ci.getCourierName() : null)
                .courierPhone(ci != null ? ci.getCourierPhone() : null)
                .status(d.getStatus())
                .createdAt(d.getCreatedAt())
                .updatedAt(d.getUpdatedAt())
                .build();
    }
}
