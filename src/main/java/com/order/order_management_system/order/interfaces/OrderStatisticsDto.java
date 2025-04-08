package com.order.order_management_system.order.interfaces;

import com.order.order_management_system.order.domain.OrderStatus;
import lombok.Value;
import java.time.LocalDate;
import java.util.Map;

@Value
public class OrderStatisticsDto {
    long totalOrders;
    double averageOrderAmount;
    Map<OrderStatus, Long> statusCounts;
    Map<LocalDate, Long> dailyOrderCounts;
}