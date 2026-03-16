package com.example.deliveryservice.controller;

import com.example.deliveryservice.dto.DeliveryRequest;
import com.example.deliveryservice.dto.DeliveryResponse;
import com.example.deliveryservice.model.DeliveryStatus;
import com.example.deliveryservice.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<DeliveryResponse> create(@RequestBody DeliveryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<DeliveryResponse>> getAll() {
        return ResponseEntity.ok(deliveryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryService.getById(id));
    }

    @GetMapping("/by-order/{orderId}")
    public ResponseEntity<List<DeliveryResponse>> getByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(deliveryService.getByOrderId(orderId));
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<DeliveryResponse>> getByStatus(@RequestParam DeliveryStatus status) {
        return ResponseEntity.ok(deliveryService.getByStatus(status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryResponse> update(@PathVariable Long id,
                                                   @RequestBody DeliveryRequest request) {
        return ResponseEntity.ok(deliveryService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<DeliveryResponse> updateStatus(@PathVariable Long id,
                                                         @RequestBody Map<String, String> body) {
        DeliveryStatus status = DeliveryStatus.valueOf(body.get("status"));
        return ResponseEntity.ok(deliveryService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deliveryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
