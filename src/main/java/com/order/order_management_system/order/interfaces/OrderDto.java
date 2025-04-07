package com.order.order_management_system.order.interfaces;

import com.order.order_management_system.order.domain.OrderStatus;
import lombok.Value;
import java.time.LocalDateTime;
import java.util.List;

@Value
public class OrderDto {
    String id;
    List<OrderItemResponse> items;
    OrderStatus status;
    LocalDateTime orderDate;
    int totalAmount;
}

@Value
class OrderItemResponse {
    String menuId;
    int quantity;
    int price;
}