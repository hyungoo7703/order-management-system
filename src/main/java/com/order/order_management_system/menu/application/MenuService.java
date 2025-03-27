package com.order.order_management_system.menu.application;

import com.order.order_management_system.menu.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
