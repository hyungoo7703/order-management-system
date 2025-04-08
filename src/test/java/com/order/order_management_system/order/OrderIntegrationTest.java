package com.order.order_management_system.order;

import com.order.order_management_system.menu.domain.MenuId;
import com.order.order_management_system.order.interfaces.OrderRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.order_management_system.order.interfaces.OrderResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void fullOrderLifecycleTest() throws Exception {
        MenuId menuId = new MenuId(UUID.randomUUID().toString());

        // 1. 주문 생성
        OrderRequest.OrderItemRequestDto item = new OrderRequest.OrderItemRequestDto(
                menuId,
                2,
                15000
        );
        OrderRequest request = new OrderRequest(List.of(item));

        MvcResult createResult = mockMvc.perform(post("/api/orders")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        OrderResponse orderResponse = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                OrderResponse.class
        );
        String orderId = orderResponse.getOrderId(); // JSON 파싱을 통한 orderId 추출

        entityManager.flush(); // 트랜잭션 강제 커밋
        entityManager.clear(); // 트랜잭션 강제 커밋

        LocalDateTime now = LocalDateTime.now();
        String startDate = now.minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String endDate = now.plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        mockMvc.perform(get("/api/orders/statistics")
                .param("startDate", startDate)
                .param("endDate", endDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalOrders").value(1))
                .andExpect(jsonPath("$.statusCounts.RECEIVED").value(1));

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