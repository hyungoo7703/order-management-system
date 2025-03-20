package com.order.order_management_system.menu.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataMenuRepository extends JpaRepository<MenuEntity, String> {
}