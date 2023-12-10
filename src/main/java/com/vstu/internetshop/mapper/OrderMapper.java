package com.vstu.internetshop.mapper;

import com.vstu.internetshop.dto.OrderDto;
import com.vstu.internetshop.dto.OrderStatus;
import com.vstu.internetshop.entity.OrderEntity;

import java.time.format.DateTimeFormatter;

public class OrderMapper {

    /**
     * Форматтер используется для форматирования даты и времени.
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Преобразует {@link OrderEntity} в {@link OrderDto}.
     *
     * @param order экземпляр {@link OrderEntity}, который нужно преобразовать.
     * @return преобразованный {@link OrderDto}.
     */
    public OrderDto toDto(OrderEntity order) {
        return new OrderDto(
                order.getId(),
                mapStatus(order.getStatus()),
                order.getUser().getId(),
                order.getUser().getUsername(),
                order.getCreatedAt().format(formatter)
        );
    }

    /**
     * Преобразует {@link OrderStatus} в строковое представление.
     *
     * @param status экземпляр {@link OrderStatus}, который нужно преобразовать.
     * @return преобразованный статус заказа в виде строки.
     */
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