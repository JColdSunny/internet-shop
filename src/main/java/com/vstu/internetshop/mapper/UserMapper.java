package com.vstu.internetshop.mapper;

import com.vstu.internetshop.dto.UserDto;
import com.vstu.internetshop.entity.RoleEntity;
import com.vstu.internetshop.entity.UserEntity;

import java.util.stream.Collectors;

/**
 * Обеспечивает преобразование объектов типа UserEntity в UserDto.
 */
public class UserMapper {

    /**
     * Преобразует объект UserEntity в UserDto.
     *
     * @param user объект UserEntity
     * @return объект UserDto соответствующий входному объекту UserEntity
     */
    public UserDto toDto(UserEntity user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.isActive() ? "Да" : "Нет",
                user.getRoles().stream()
                        .map(RoleEntity::getName)
                        .map(UserMapper::mapRole)
                        .collect(Collectors.joining(", "))
        );
    }

    /**
     * Преобразует строковое представление роли в русифицированную строку.
     *
     * @param role строковое представление роли
     * @return русифицированное строковое представление роли
     */
    private static String mapRole(String role) {
        return switch (role) {
            case "ADMIN" -> "Администратор";
            case "USER" -> "Пользователь";
            case "COURIER" -> "Курьер";
            default -> "Неизвестная роль";
        };
    }

}