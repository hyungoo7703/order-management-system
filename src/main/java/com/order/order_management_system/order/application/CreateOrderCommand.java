package com.order.order_management_system.order.application;

import lombok.Value;

import java.util.List;

@Value
public class CreateOrderCommand {
    List<OrderItemRequest> items;
}