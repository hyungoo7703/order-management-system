package com.order.order_management_system.order.domain;

import com.order.order_management_system.order.domain.OrderStatus;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Order {
    private final OrderId id;
    private final List<OrderItem> items;
    private OrderStatus status;
    private final LocalDateTime orderDate;
    private int totalAmount;

    public Order(OrderId id, List<OrderItem> items) {
        this.id = id;
        this.items = List.copyOf(items);
        this.status = OrderStatus.RECEIVED;
        this.orderDate = LocalDateTime.now();
        this.totalAmount = calculateTotalAmount();
    }

    public void startProcessing() {
        if (status != OrderStatus.RECEIVED) {
            throw new IllegalStateException("Only received orders can be processed");
        }
        status = OrderStatus.PROCESSING;
    }

    public void complete() {
        if (status != OrderStatus.PROCESSING) {
            throw new IllegalStateException("Only processing orders can be completed");
        }
        status = OrderStatus.COMPLETED;
    }

    public void cancel() {
        if (status == OrderStatus.COMPLETED) {
            throw new IllegalStateException("Completed orders cannot be cancelled");
        }
        status = OrderStatus.CANCELLED;
    }

    private int calculateTotalAmount() {
        return items.stream()
                .mapToInt(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}