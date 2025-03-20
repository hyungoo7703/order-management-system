package com.order.order_management_system.menu.infrastructure;

import com.order.order_management_system.menu.domain.MenuStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "menu_items")
@Getter
@Setter
@NoArgsConstructor
public class MenuEntity {
    @Id
    private String id;

    @Column(name = "menu_name")
    private String name;

    @Column(name = "menu_description")
    private String description;

    private int price;

    private String currency;

    // H2의 Enum 제약을 고려하여 String으로 저장
    @Enumerated(EnumType.STRING)
    @Column(name = "menu_status", length = 20)
    private MenuStatus status;
}