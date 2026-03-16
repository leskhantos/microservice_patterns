package com.example.orderservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Aggregate Root — owns the Order lifecycle and all OrderItems.
 * Invariants enforced here: cannot add items to a CANCELLED order, etc.
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @Column(nullable = false)
    private String customerId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street",     column = @Column(name = "ship_street")),
            @AttributeOverride(name = "city",       column = @Column(name = "ship_city")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "ship_postal_code")),
            @AttributeOverride(name = "country",    column = @Column(name = "ship_country"))
    })
    private Address shippingAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Order(String orderNumber, String customerId, Address shippingAddress) {
        this.orderNumber = orderNumber;
        this.customerId = customerId;
        this.shippingAddress = shippingAddress;
        this.status = OrderStatus.PENDING;
    }

    // --- Aggregate behaviour ---

    public void addItem(OrderItem item) {
        if (this.status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot add items to a cancelled order");
        }
        item.setOrder(this);
        this.items.add(item);
    }

    public void removeItem(Long itemId) {
        this.items.removeIf(i -> i.getId().equals(itemId));
    }

    public void confirm() {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("Only PENDING orders can be confirmed");
        }
        this.status = OrderStatus.CONFIRMED;
    }

    public void cancel() {
        if (this.status == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Delivered orders cannot be cancelled");
        }
        this.status = OrderStatus.CANCELLED;
    }

    public Money totalAmount() {
        return items.stream()
                .map(OrderItem::totalPrice)
                .reduce(new Money(BigDecimal.ZERO, "USD"), Money::add);
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}
