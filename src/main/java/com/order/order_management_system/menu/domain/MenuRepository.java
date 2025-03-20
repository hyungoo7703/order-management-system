package com.order.order_management_system.menu.domain;

import java.util.List;
import java.util.Optional;

public interface MenuRepository {
    Menu save(Menu menu);
    Optional<Menu> findById(MenuId id);
    List<Menu> findAll();
    void delete(MenuId id);
}