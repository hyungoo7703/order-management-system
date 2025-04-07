package com.order.order_management_system.order.interfaces;

import com.order.order_management_system.order.application.*;
import com.order.order_management_system.order.domain.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "주문 관리", description = "주문 생성, 조회, 상태 변경 API")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Operation(
            summary = "주문 생성",
            description = "새로운 주문을 생성합니다",
            responses = {
                    @ApiResponse(responseCode = "201", description = "주문 생성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 입력 값")
            }
    )
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request
    ) {
        CreateOrderCommand command = new CreateOrderCommand(
                request.getItems().stream()
                        .map(item -> new OrderItemRequest(
                                item.getMenuId(),
                                item.getQuantity(),
                                item.getPrice()
                        )).toList()
        );

        OrderId orderId = orderService.createOrder(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new OrderResponse(orderId.getValue()));
    }

    @Operation(
            summary = "전체 주문 조회",
            description = "모든 주문 목록을 조회합니다"
    )
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(
                orderService.getAllOrders().stream()
                        .map(this::toDto)
                        .toList()
        );
    }

    @Operation(
            summary = "주문 상태별 조회",
            description = "특정 상태의 주문 목록을 조회합니다"
    )
    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderDto>> getOrdersByStatus(
            @Parameter(description = "주문 상태", example = "PROCESSING")
            @PathVariable OrderStatus status
    ) {
        return ResponseEntity.ok(
                orderService.getOrdersByStatus(status).stream()
                        .map(this::toDto)
                        .toList()
        );
    }

    @Operation(
            summary = "주문 상세 조회",
            description = "특정 ID의 주문 상세 정보를 조회합니다"
    )
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(
            @Parameter(description = "주문 ID", example = "uuid-string")
            @PathVariable String id
    ) {
        return ResponseEntity.ok(
                toDto(orderService.getOrderById(new OrderId(id)))
        );
    }

    @Operation(
            summary = "주문 상태 업데이트",
            description = "주문 상태를 변경합니다"
    )
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateOrderStatus(
            @Parameter(description = "주문 ID", example = "uuid-string")
            @PathVariable String id,
            @RequestParam OrderStatus status
    ) {
        orderService.updateOrderStatus(new UpdateOrderStatusCommand(id, status));
        return ResponseEntity.noContent().build();
    }

    private OrderDto toDto(Order order) {
        return new OrderDto(
                order.getId().getValue(),
                order.getItems().stream()
                        .map(item -> new OrderItemResponse(
                                item.getMenuId().getValue(),
                                item.getQuantity(),
                                item.getPrice()
                        )).toList(),
                order.getStatus(),
                order.getOrderDate(),
                order.getTotalAmount()
        );
    }
}