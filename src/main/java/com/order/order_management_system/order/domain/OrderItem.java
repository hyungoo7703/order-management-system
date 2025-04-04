package com.order.order_management_system.order.domain;

import com.order.order_management_system.menu.domain.MenuId;
import lombok.Value;

@Value
public class OrderItem {
    MenuId menuId;
    int quantity;
    int price; // 주문 시점의 메뉴 가격 (변동 방지)
}