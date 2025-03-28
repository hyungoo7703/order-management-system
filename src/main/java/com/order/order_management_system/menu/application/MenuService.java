package com.order.order_management_system.menu.application;

import com.order.order_management_system.menu.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;

    @Transactional
    public MenuId createMenu(CreateMenuCommand command) {
        MenuId menuId = new MenuId(UUID.randomUUID().toString());
        Price price = new Price(command.getPrice(), command.getCurrency());
        Menu menu = new Menu(menuId, command.getName(), command.getDescription(), price, command.getStatus());
        menuRepository.save(menu);
        return menuId;
    }

    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    public Menu getMenuById(MenuId id) {
        return menuRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Menu not found with id: " + id.getValue()));
    }

    @Transactional
    public void updateMenu(UpdateMenuCommand command) {
        MenuId menuId = new MenuId(command.getId());
        Menu existingMenu = getMenuById(menuId);

        Price price = new Price(command.getPrice(), command.getCurrency());
        Menu updatedMenu = new Menu(
                menuId,
                command.getName(),
                command.getDescription(),
                price,
                command.getStatus()
        );

        menuRepository.save(updatedMenu);
    }

    @Transactional
    public void deleteMenu(MenuId id) {
        menuRepository.delete(id);
    }
}
