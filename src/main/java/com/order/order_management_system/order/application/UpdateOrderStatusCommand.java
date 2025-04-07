package com.order.order_management_system.order.application;

import com.order.order_management_system.order.domain.OrderStatus;
import lombok.Value;

@Value
public class UpdateOrderStatusCommand {
    String orderId;
    OrderStatus status;
}