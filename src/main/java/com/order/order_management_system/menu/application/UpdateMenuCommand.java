package com.order.order_management_system.menu.application;

import com.order.order_management_system.menu.domain.MenuStatus;
import lombok.Value;

@Value
public class UpdateMenuCommand {
    String id;
    String name;
    String description;
    int price;
    String currency;
    MenuStatus status;
}