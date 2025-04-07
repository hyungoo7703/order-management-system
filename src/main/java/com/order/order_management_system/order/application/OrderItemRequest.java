package com.order.order_management_system.order.application;

import com.order.order_management_system.menu.domain.MenuId;
import lombok.Value;

@Value
public class OrderItemRequest {
    MenuId menuId;
    int quantity;
    int price;
}