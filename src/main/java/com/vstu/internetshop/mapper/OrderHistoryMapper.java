package com.vstu.internetshop.mapper;

import com.vstu.internetshop.dto.OrderHistoryDto;
import com.vstu.internetshop.dto.OrderStatus;
import com.vstu.internetshop.entity.OrderEntity;
import com.vstu.internetshop.entity.ProductEntity;

import java.math.BigDecimal;
import java.util.List;

public final class OrderHistoryMapper {

    public OrderHistoryDto toDto(OrderEntity order, List<ProductEntity> products) {
        int count = 0;
        BigDecimal price = BigDecimal.ZERO;

        for (ProductEntity product : products) {
            count++;
            price = price.add(product.getPrice());
        }

        return new OrderHistoryDto(count, price, mapStatus(order.getStatus()));
    }

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
