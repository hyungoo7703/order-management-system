package com.order.order_management_system.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(OrderId id);
    List<Order> findAll();
    List<Order> findByStatus(OrderStatus status);
    OrderStatistics calculateStatistics(LocalDateTime startDate, LocalDateTime endDate);
}