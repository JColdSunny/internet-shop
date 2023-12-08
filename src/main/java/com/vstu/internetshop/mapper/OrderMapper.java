package com.vstu.internetshop.mapper;

import com.vstu.internetshop.dto.OrderDto;
import com.vstu.internetshop.dto.OrderStatus;
import com.vstu.internetshop.entity.OrderEntity;

import java.time.format.DateTimeFormatter;

public class OrderMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public OrderDto toDto(OrderEntity order) {
        return new OrderDto(
                order.getId(),
                mapStatus(order.getStatus()),
                order.getUser().getId(),
                order.getUser().getUsername(),
                order.getCreatedAt().format(formatter)
        );
    }

    private static String mapStatus(OrderStatus status) {
        return switch (status) {
            case CREATED -> "Не оплачен";
            case PROCESSING -> "Оплачен";
            case CONFIRMED -> "Зарегестрирован";
            case CANCELED -> "Отменен";
            case IN_DELIVERY -> "В пути";
            case DELIVERED -> "Доставлен";
        };
    }

}