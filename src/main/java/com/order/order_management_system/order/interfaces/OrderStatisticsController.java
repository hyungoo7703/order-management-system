package com.order.order_management_system.order.interfaces;

import com.order.order_management_system.order.application.OrderStatisticsService;
import com.order.order_management_system.order.domain.OrderStatistics;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@Tag(name = "주문 통계", description = "주문 관련 통계 데이터 제공 API")
@RestController
@RequestMapping("/api/orders/statistics")
@RequiredArgsConstructor
public class OrderStatisticsController {
    private final OrderStatisticsService statisticsService;

    @Operation(
            summary = "주문 통계 조회",
            description = "지정한 기간 동안의 주문 통계를 제공합니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "통계 조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 날짜 형식")
            }
    )
    @GetMapping
    public ResponseEntity<OrderStatisticsDto> getOrderStatistics(
            @Parameter(description = "시작일 (형식: yyyy-MM-dd'T'HH:mm)", example = "2024-01-01T00:00")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate,

            @Parameter(description = "종료일 (형식: yyyy-MM-dd'T'HH:mm)", example = "2024-01-31T23:59")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDate
    ) {
        return ResponseEntity.ok(
                convertToDto(statisticsService.getStatistics(startDate, endDate))
        );
    }

    private OrderStatisticsDto convertToDto(OrderStatistics statistics) {
        return new OrderStatisticsDto(
                statistics.getTotalOrders(),
                statistics.getAverageOrderAmount(),
                statistics.getStatusCounts(),
                statistics.getDailyOrderCounts()
        );
    }
}