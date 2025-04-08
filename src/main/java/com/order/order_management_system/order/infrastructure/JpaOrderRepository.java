package com.order.order_management_system.order.infrastructure;

import com.order.order_management_system.menu.domain.*;
import com.order.order_management_system.order.domain.*;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaOrderRepository implements OrderRepository {
    private final SpringDataOrderRepository springDataOrderRepository;

    @Override
    public Order save(Order order) {
        OrderEntity entity = toEntity(order);
        OrderEntity saved = springDataOrderRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Order> findById(OrderId id) {
        return springDataOrderRepository.findById(id.getValue())
                .map(this::toDomain);
    }

    @Override
    public List<Order> findAll() {
        return springDataOrderRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return springDataOrderRepository.findByStatus(status).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public OrderStatistics calculateStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        Long totalOrders = springDataOrderRepository.countByOrderDateBetween(startDate, endDate);

        Double averageAmount = springDataOrderRepository.getAverageAmountByDateRange(startDate, endDate);

        List<Object[]> statusCounts = springDataOrderRepository.countByStatusGrouped(startDate, endDate);
        Map<OrderStatus, Long> statusCountMap = statusCounts.stream()
                .collect(Collectors.toMap(
                        arr -> (OrderStatus) arr[0],
                        arr -> (Long) arr[1]
                ));

        List<Object[]> dailyCounts = springDataOrderRepository.countDailyOrders(startDate, endDate);
        Map<LocalDate, Long> dailyCountMap = dailyCounts.stream()
                .collect(Collectors.toMap(
                        arr -> ((java.sql.Date) arr[0]).toLocalDate(),
                        arr -> (Long) arr[1]
                ));

        return new OrderStatistics(
                totalOrders,
                averageAmount != null ? averageAmount : 0.0,
                statusCountMap,
                dailyCountMap
        );
    }

    private OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId().getValue());
        entity.setItems(order.getItems().stream()
                .map(item -> new OrderItemEmbeddable(
                        item.getMenuId().getValue(),
                        item.getQuantity(),
                        item.getPrice()
                )).toList());
        entity.setStatus(order.getStatus());
        entity.setOrderDate(order.getOrderDate());
        entity.setTotalAmount(order.getTotalAmount());
        return entity;
    }

    private Order toDomain(OrderEntity entity) {
        List<OrderItem> items = entity.getItems().stream()
                .map(item -> new OrderItem(
                        new MenuId(item.getMenuId()),
                        item.getQuantity(),
                        item.getPrice()
                )).toList();
        return new Order(
                new OrderId(entity.getId()),
                items
        );
    }
}