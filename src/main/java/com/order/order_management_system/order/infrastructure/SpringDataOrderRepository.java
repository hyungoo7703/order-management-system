package com.order.order_management_system.order.infrastructure;

import com.order.order_management_system.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SpringDataOrderRepository extends JpaRepository<OrderEntity, String> {
    List<OrderEntity> findByStatus(OrderStatus status);
}