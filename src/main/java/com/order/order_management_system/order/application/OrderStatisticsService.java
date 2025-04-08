package com.order.order_management_system.order.application;

import com.order.order_management_system.order.domain.OrderStatistics;
import com.order.order_management_system.order.domain.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderStatisticsService {
    private final OrderRepository orderRepository;

    public OrderStatistics getStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.calculateStatistics(
                startDate != null ? startDate : LocalDateTime.now().minusMonths(1),
                endDate != null ? endDate : LocalDateTime.now()
        );
    }
}