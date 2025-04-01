package com.order.order_management_system.menu.interfaces;

import com.order.order_management_system.menu.application.CreateMenuCommand;
import com.order.order_management_system.menu.application.MenuService;
import com.order.order_management_system.menu.application.UpdateMenuCommand;
import com.order.order_management_system.menu.domain.Menu;
import com.order.order_management_system.menu.domain.MenuId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<MenuResponse> createMenu(@Valid @RequestBody MenuRequest request) {
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

    @GetMapping
    public ResponseEntity<List<MenuDto>> getAllMenus() {
        List<Menu> menus = menuService.getAllMenus();
        List<MenuDto> menuDtos = menus.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(menuDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuDto> getMenuById(@PathVariable String id) {
        Menu menu = menuService.getMenuById(new MenuId(id));
        return ResponseEntity.ok(toDto(menu));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMenu(
            @PathVariable String id,
            @Valid @RequestBody MenuRequest request) {

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable String id) {
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