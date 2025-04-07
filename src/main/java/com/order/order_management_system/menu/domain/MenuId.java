package com.order.order_management_system.menu.domain;

        import com.fasterxml.jackson.annotation.JsonCreator;
        import com.fasterxml.jackson.annotation.JsonValue;
        import lombok.Value;

@Value
public class MenuId {
    @JsonValue
    private final String value; // String인 이유: 보통 DDD에서는 UUID(Universally Unique Identifier)를 사용하여 ID를 생성

    @JsonCreator
    public MenuId(String value) {
        this.value = value;
    }
}
