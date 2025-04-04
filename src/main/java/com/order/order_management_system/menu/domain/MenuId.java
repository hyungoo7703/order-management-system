package com.order.order_management_system.menu.domain;

import lombok.Value;

@Value
public class MenuId {
    String value; // String인 이유: 보통 DDD에서는 UUID(Universally Unique Identifier)를 사용하여 ID를 생성
}