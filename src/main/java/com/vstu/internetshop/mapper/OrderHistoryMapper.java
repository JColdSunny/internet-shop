package com.vstu.internetshop.mapper;

import com.vstu.internetshop.dto.OrderHistoryDto;
import com.vstu.internetshop.dto.OrderStatus;
import com.vstu.internetshop.entity.OrderEntity;
import com.vstu.internetshop.entity.ProductEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * Класс для преобразования объектов типа OrderEntity в объекты типа OrderHistoryDto.
 */
public final class OrderHistoryMapper {

    /**
     * Метод преобразует данные об заказе и продуктах в объект типа OrderHistoryDto.
     *
     * @param order    объект типа OrderEntity, содержащий информацию о заказе.
     * @param products список объектов типа ProductEntity, содержащий информацию о продуктах.
     * @return объект типа OrderHistoryDto, содержащий информацию о заказе и продуктах.
     */
    public OrderHistoryDto toDto(OrderEntity order, List<ProductEntity> products) {
        int count = 0;
        BigDecimal price = BigDecimal.ZERO;

        for (ProductEntity product : products) {
            count++;
            price = price.add(product.getPrice());
        }

        return new OrderHistoryDto(count, price, mapStatus(order.getStatus()));
    }

    /**
     * Вспомогательный метод для преобразования статуса заказа из формата OrderStatus в строковый формат.
     *
     * @param status статус заказа в формате OrderStatus.
     * @return статус заказа в строковом формате.
     */
    private static String mapStatus(OrderStatus status) {
        return switch (status) {
            case CREATED -> "Создан";
            case PROCESSING -> "В обработке";
            case CONFIRMED -> "Подтвержден";
            case CANCELED -> "Отменен";
            case IN_DELIVERY -> "В пути";
            case DELIVERED -> "Доставлен";
        };
    }
}