package com.order.order_management_system.menu.interfaces;

import com.order.order_management_system.menu.application.CreateMenuCommand;
import com.order.order_management_system.menu.application.MenuService;
import com.order.order_management_system.menu.application.UpdateMenuCommand;
import com.order.order_management_system.menu.domain.Menu;
import com.order.order_management_system.menu.domain.MenuId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "메뉴 관리", description = "메뉴 생성, 조회, 수정, 삭제 API")
@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    /**
     * 새로운 메뉴 생성
     * @param request 메뉴 생성 요청 데이터 (이름, 설명, 가격, 상태)
     * @return 생성된 메뉴 ID
     */
    @Operation(
            summary = "메뉴 생성",
            description = "새로운 메뉴 항목을 시스템에 등록합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "메뉴 생성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 입력 값")
            }
    )
    @PostMapping
    public ResponseEntity<MenuResponse> createMenu(
            @Valid @RequestBody MenuRequest request
    ) {
        CreateMenuCommand command = new CreateMenuCommand(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                "KRW",
                request.getStatus()
        );

        MenuId menuId = menuService.createMenu(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MenuResponse(menuId.getValue()));
    }

    /**
     * 전체 메뉴 목록 조회
     * @return 모든 메뉴 정보 목록
     */
    @Operation(
            summary = "전체 메뉴 조회",
            description = "시스템에 등록된 모든 메뉴 목록을 조회합니다."
    )
    @GetMapping
    public ResponseEntity<List<MenuDto>> getAllMenus() {
        List<Menu> menus = menuService.getAllMenus();
        List<MenuDto> menuDtos = menus.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(menuDtos);
    }

    /**
     * 단일 메뉴 상세 조회
     * @param id 조회할 메뉴 ID
     * @return 해당 메뉴 상세 정보
     */
    @Operation(
            summary = "단일 메뉴 조회",
            description = "특정 ID를 가진 메뉴의 상세 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "404", description = "메뉴를 찾을 수 없음")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<MenuDto> getMenuById(
            @Parameter(description = "메뉴 ID", example = "uuid-string")
            @PathVariable String id
    ) {
        Menu menu = menuService.getMenuById(new MenuId(id));
        return ResponseEntity.ok(toDto(menu));
    }

    /**
     * 메뉴 정보 수정
     * @param id 수정할 메뉴 ID
     * @param request 수정할 메뉴 데이터
     */
    @Operation(
            summary = "메뉴 수정",
            description = "기존 메뉴 정보를 업데이트합니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "수정 성공"),
                    @ApiResponse(responseCode = "404", description = "메뉴를 찾을 수 없음")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMenu(
            @Parameter(description = "메뉴 ID", example = "uuid-string")
            @PathVariable String id,
            @Valid @RequestBody MenuRequest request
    ) {
        UpdateMenuCommand command = new UpdateMenuCommand(
                id,
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                "KRW",
                request.getStatus()
        );

        menuService.updateMenu(command);
        return ResponseEntity.noContent().build();
    }

    /**
     * 메뉴 삭제
     * @param id 삭제할 메뉴 ID
     */
    @Operation(
            summary = "메뉴 삭제",
            description = "특정 ID를 가진 메뉴를 시스템에서 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "삭제 성공"),
                    @ApiResponse(responseCode = "404", description = "메뉴를 찾을 수 없음")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(
            @Parameter(description = "메뉴 ID", example = "uuid-string")
            @PathVariable String id
    ) {
        menuService.deleteMenu(new MenuId(id));
        return ResponseEntity.noContent().build();
    }

    private MenuDto toDto(Menu menu) {
        return new MenuDto(
                menu.getId().getValue(),
                menu.getName(),
                menu.getDescription(),
                menu.getPrice().getAmount(),
                menu.getPrice().getCurrency(),
                menu.getStatus().toString()
        );
    }
}