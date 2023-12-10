package com.vstu.internetshop.mapper;

import com.vstu.internetshop.dto.ProductDto;
import com.vstu.internetshop.dto.ProductStatus;
import com.vstu.internetshop.entity.ProductEntity;

/**
 * Класс-маппер для преобразования между классами ProductEntity и ProductDto
 */
public class ProductMapper {

    /**
     * Преобразует объект ProductEntity в ProductDto
     *
     * @param entity объект ProductEntity для преобразования
     * @return Преобразованный объект ProductDto
     */
    public ProductDto toDto(ProductEntity entity) {
        return new ProductDto(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getDescription(),
                entity.getCreatedUser().getUsername(),
                mapStatus(entity.getStatus())
        );
    }

    /**
     * Маппер статуса продукта в строку
     *
     * @param status статус продукта
     * @return Статус продукта в виде строки
     */
    private String mapStatus(ProductStatus status) {
        return switch (status) {
            case AVAILABLE -> "Доступен";
            case IN_ORDER -> "В заказе";
            case PAID -> "Оплачен";
        };
    }

}