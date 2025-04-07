package com.order.order_management_system;

import com.order.order_management_system.order.domain.OrderStatus;
import com.order.order_management_system.order.interfaces.OrderRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void fullOrderLifecycleTest() throws Exception {
        // 1. 주문 생성
        OrderRequest.OrderItemRequestDto item = new OrderRequest.OrderItemRequestDto(
                new com.order.order_management_system.menu.domain.MenuId("통합 테스트 메뉴"),
                2,
                15000
        );
        OrderRequest request = new OrderRequest(List.of(item));

        String orderId = mockMvc.perform(post("/api/orders")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString()
                .split("\"")[3]; // JSON 응답에서 ID 추출

        // 2. 주문 조회
        mockMvc.perform(get("/api/orders/" + orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RECEIVED"));

        // 3. 주문 상태 업데이트
        mockMvc.perform(patch("/api/orders/" + orderId + "/status")
                        .param("status", "PROCESSING"))
                .andExpect(status().isNoContent());

        // 4. 상태별 조회 확인
        mockMvc.perform(get("/api/orders/status/PROCESSING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(orderId));
    }
}