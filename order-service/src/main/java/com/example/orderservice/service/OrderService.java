package com.example.orderservice.service;

import com.example.orderservice.domain.Address;
import com.example.orderservice.domain.Money;
import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderItem;
import com.example.orderservice.domain.OrderStatus;
import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.OrderItemDto;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.dto.UpdateOrderRequest;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderResponse createOrder(CreateOrderRequest request) {
        Address address = new Address(
                request.getStreet(),
                request.getCity(),
                request.getPostalCode(),
                request.getCountry()
        );
        String orderNumber = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Order order = new Order(orderNumber, request.getCustomerId(), address);

        if (request.getItems() != null) {
            for (CreateOrderRequest.CreateOrderItemRequest itemReq : request.getItems()) {
                Money unitPrice = new Money(itemReq.getUnitPriceAmount(), itemReq.getUnitPriceCurrency());
                OrderItem item = new OrderItem(
                        itemReq.getProductId(),
                        itemReq.getProductName(),
                        itemReq.getQuantity(),
                        unitPrice
                );
                order.addItem(item);
            }
        }

        return toResponse(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = findById(id);
        return toResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByCustomer(String customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse updateOrder(Long id, UpdateOrderRequest request) {
        Order order = findById(id);
        Address address = new Address(
                request.getStreet(),
                request.getCity(),
                request.getPostalCode(),
                request.getCountry()
        );
        order.setShippingAddress(address);
        return toResponse(orderRepository.save(order));
    }

    public OrderResponse confirmOrder(Long id) {
        Order order = findById(id);
        order.confirm();
        return toResponse(orderRepository.save(order));
    }

    public OrderResponse cancelOrder(Long id) {
        Order order = findById(id);
        order.cancel();
        return toResponse(orderRepository.save(order));
    }

    public void deleteOrder(Long id) {
        Order order = findById(id);
        orderRepository.delete(order);
    }

    // --- helpers ---

    private Order findById(Long id) {
        return orderRepository.findByIdWithItems(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    private OrderResponse toResponse(Order order) {
        OrderResponse resp = new OrderResponse();
        resp.setId(order.getId());
        resp.setOrderNumber(order.getOrderNumber());
        resp.setCustomerId(order.getCustomerId());
        resp.setStatus(order.getStatus());
        resp.setCreatedAt(order.getCreatedAt());
        resp.setUpdatedAt(order.getUpdatedAt());

        if (order.getShippingAddress() != null) {
            resp.setStreet(order.getShippingAddress().getStreet());
            resp.setCity(order.getShippingAddress().getCity());
            resp.setPostalCode(order.getShippingAddress().getPostalCode());
            resp.setCountry(order.getShippingAddress().getCountry());
        }

        List<OrderItemDto> itemDtos = order.getItems().stream().map(item -> {
            OrderItemDto dto = new OrderItemDto();
            dto.setId(item.getId());
            dto.setProductId(item.getProductId());
            dto.setProductName(item.getProductName());
            dto.setQuantity(item.getQuantity());
            if (item.getUnitPrice() != null) {
                dto.setUnitPriceAmount(item.getUnitPrice().getAmount());
                dto.setUnitPriceCurrency(item.getUnitPrice().getCurrency());
                dto.setTotalPriceAmount(item.totalPrice().getAmount());
            }
            return dto;
        }).collect(Collectors.toList());
        resp.setItems(itemDtos);

        Money total = order.totalAmount();
        resp.setTotalAmount(total.getAmount());
        resp.setTotalCurrency(total.getCurrency());

        return resp;
    }
}
