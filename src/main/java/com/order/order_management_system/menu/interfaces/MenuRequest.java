package com.order.order_management_system.menu.interfaces;

import com.order.order_management_system.menu.domain.MenuStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MenuRequest {
    @NotBlank(message = "이름은 필수 항목입니다")
    private String name;

    private String description;

    @NotNull(message = "가격은 필수 항목입니다")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다")
    private Integer price;

    @NotNull(message = "상태는 필수 항목입니다")
    private MenuStatus status;
}