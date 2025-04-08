package com.order.order_management_system.order.domain;

import lombok.Value;
import java.time.LocalDate;
import java.util.Map;

@Value
public class OrderStatistics {
    long totalOrders;
    double averageOrderAmount;
    Map<OrderStatus, Long> statusCounts;
    Map<LocalDate, Long> dailyOrderCounts;
}