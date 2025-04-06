package com.order.order_management_system.order.infrastructure;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class OrderItemEmbeddable { // OrderItem은 독립적인 생명주기를 가지지 않으므로 임베디드 타입으로 설계
    private String menuId;
    private int quantity;
    private int price;

    public OrderItemEmbeddable(String menuId, int quantity, int price) {
        this.menuId = menuId;
        this.quantity = quantity;
        this.price = price;
    }
}