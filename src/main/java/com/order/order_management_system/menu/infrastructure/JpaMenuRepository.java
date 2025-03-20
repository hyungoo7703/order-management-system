package com.order.order_management_system.menu.infrastructure;

import com.order.order_management_system.menu.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaMenuRepository implements MenuRepository {
    private final SpringDataMenuRepository springDataMenuRepository;

    @Override
    public Menu save(Menu menu) {
        MenuEntity entity = toEntity(menu);
        MenuEntity savedEntity = springDataMenuRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Menu> findById(MenuId id) {
        return springDataMenuRepository.findById(id.getValue())
                .map(this::toDomain);
    }

    @Override
    public List<Menu> findAll() {
        return springDataMenuRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(MenuId id) {
        springDataMenuRepository.deleteById(id.getValue());
    }

    private MenuEntity toEntity(Menu menu) {
        MenuEntity entity = new MenuEntity();
        entity.setId(menu.getId().getValue());
        entity.setName(menu.getName());
        entity.setDescription(menu.getDescription());
        entity.setPrice(menu.getPrice().getAmount());
        entity.setCurrency(menu.getPrice().getCurrency());
        entity.setStatus(menu.getStatus());
        return entity;
    }

    private Menu toDomain(MenuEntity entity) {
        return new Menu(
                new MenuId(entity.getId()),
                entity.getName(),
                entity.getDescription(),
                new Price(entity.getPrice(), entity.getCurrency()),
                entity.getStatus()
        );
    }
}