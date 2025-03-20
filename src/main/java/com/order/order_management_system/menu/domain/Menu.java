package com.order.order_management_system.menu.domain;

import lombok.Getter;

@Getter
public class Menu {
    private final MenuId id;
    private final String name;
    private final String description;
    private final Price price;
    private final MenuStatus status;

    public Menu(MenuId id, String name, String description, Price price, MenuStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
    }

    public boolean isAvailable() {
        return status == MenuStatus.AVAILABLE;
    }
}