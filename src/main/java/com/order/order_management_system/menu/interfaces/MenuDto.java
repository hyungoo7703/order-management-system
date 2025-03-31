package com.order.order_management_system.menu.interfaces;

import lombok.Value;

@Value
public class MenuDto {
    String id;
    String name;
    String description;
    int price;
    String currency;
    String status;
}