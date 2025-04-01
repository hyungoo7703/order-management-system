package com.order.order_management_system.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.order_management_system.menu.domain.MenuStatus;
import com.order.order_management_system.menu.interfaces.MenuRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MenuIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void menuCrudOperations() throws Exception {
        // 1. 메뉴 생성
        MenuRequest createRequest = new MenuRequest();
        createRequest.setName("통합 테스트 메뉴");
        createRequest.setDescription("통합 테스트 설명");
        createRequest.setPrice(10000);
        createRequest.setStatus(MenuStatus.AVAILABLE);

        String response = mockMvc.perform(post("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        String menuId = objectMapper.readTree(response).get("id").asText();

        // 2. 생성된 메뉴 조회
        mockMvc.perform(get("/api/menus/" + menuId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(menuId))
                .andExpect(jsonPath("$.name").value("통합 테스트 메뉴"));

        // 3. 메뉴 업데이트
        MenuRequest updateRequest = new MenuRequest();
        updateRequest.setName("수정된 메뉴");
        updateRequest.setDescription("수정된 설명");
        updateRequest.setPrice(15000);
        updateRequest.setStatus(MenuStatus.AVAILABLE);

        mockMvc.perform(put("/api/menus/" + menuId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNoContent());

        // 4. 업데이트된 메뉴 확인
        mockMvc.perform(get("/api/menus/" + menuId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("수정된 메뉴"))
                .andExpect(jsonPath("$.price").value(15000));

        // 5. 메뉴 삭제
        mockMvc.perform(delete("/api/menus/" + menuId))
                .andExpect(status().isNoContent());

        // 6. 삭제된 메뉴 조회 시 404 확인
        mockMvc.perform(get("/api/menus/" + menuId))
                .andExpect(status().isNotFound());
    }
}