package com.order.order_management_system.order.interfaces;

import com.order.order_management_system.menu.domain.MenuId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import java.util.List;

@Value
public class OrderRequest {
    @Valid
    @NotNull(message = "주문 항목은 필수입니다")
    List<OrderItemRequestDto> items;

    @Value
    public static class OrderItemRequestDto {
        @NotNull(message = "메뉴 ID는 필수입니다")
        MenuId menuId;

        @Min(value = 1, message = "수량은 1 이상이어야 합니다")
        int quantity;

        @Min(value = 0, message = "가격은 0 이상이어야 합니다")
        int price;
    }
}