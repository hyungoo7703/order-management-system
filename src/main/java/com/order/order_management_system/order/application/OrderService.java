package com.order.order_management_system.order.application;

import com.order.order_management_system.order.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;

    @Transactional
    public OrderId createOrder(CreateOrderCommand command) {
        List<OrderItem> items = command.getItems().stream()
                .map(item -> new OrderItem(
                        item.getMenuId(),
                        item.getQuantity(),
                        item.getPrice()
                )).toList();

        OrderId orderId = new OrderId(UUID.randomUUID().toString());
        Order order = new Order(orderId, items);
        orderRepository.save(order);
        return orderId;
    }

    @Transactional
    public void updateOrderStatus(UpdateOrderStatusCommand command) {
        OrderId orderId = new OrderId(command.getOrderId());
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        switch (command.getStatus()) {
            case PROCESSING -> order.startProcessing();
            case COMPLETED -> order.complete();
            case CANCELLED -> order.cancel();
            default -> throw new IllegalArgumentException("Invalid status transition");
        }

        orderRepository.save(order);
    }

    public Order getOrderById(OrderId id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
}