package com.vstu.internetshop.mapper;

import com.vstu.internetshop.dto.UserDto;
import com.vstu.internetshop.entity.RoleEntity;
import com.vstu.internetshop.entity.UserEntity;

import java.util.stream.Collectors;

public class UserMapper {

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

    private static String mapRole(String role) {
        return switch (role) {
            case "ADMIN" -> "Администратор";
            case "USER" -> "Пользователь";
            case "COURIER" -> "Курьер";
            default -> "Неизвестная роль";
        };
    }

}
