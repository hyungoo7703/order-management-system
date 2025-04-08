package com.order.order_management_system.order.infrastructure;

import com.order.order_management_system.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface SpringDataOrderRepository extends JpaRepository<OrderEntity, String> {
    List<OrderEntity> findByStatus(OrderStatus status);

    /**
     * 통계 쿼리
     */
    // 1. 총 주문 수
    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.orderDate BETWEEN ?1 AND ?2")
    Long countByOrderDateBetween(LocalDateTime start, LocalDateTime end);

    // 2. 평균 주문 금액
    @Query("SELECT AVG(o.totalAmount) FROM OrderEntity o WHERE o.orderDate BETWEEN ?1 AND ?2")
    Double getAverageAmountByDateRange(LocalDateTime start, LocalDateTime end);

    // 3. 상태별 주문 수
    @Query("SELECT o.status AS status, COUNT(o) AS count " +
            "FROM OrderEntity o " +
            "WHERE o.orderDate BETWEEN ?1 AND ?2 " +
            "GROUP BY o.status")
    List<Object[]> countByStatusGrouped(LocalDateTime start, LocalDateTime end);

    // 4. 일별 주문 수
    @Query("SELECT CAST(o.orderDate AS date) AS orderDate, COUNT(o) AS count " +
            "FROM OrderEntity o " +
            "WHERE o.orderDate BETWEEN ?1 AND ?2 " +
            "GROUP BY CAST(o.orderDate AS date)")
    List<Object[]> countDailyOrders(LocalDateTime start, LocalDateTime end);
}