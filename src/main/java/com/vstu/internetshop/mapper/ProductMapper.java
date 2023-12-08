package com.vstu.internetshop.mapper;

import com.vstu.internetshop.dto.ProductDto;
import com.vstu.internetshop.dto.ProductStatus;
import com.vstu.internetshop.entity.ProductEntity;

public class ProductMapper {

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

    private String mapStatus(ProductStatus status) {
        return switch (status) {
            case AVAILABLE -> "Доступен";
            case IN_ORDER -> "В заказе";
            case PAID -> "Оплачен";
        };
    }

}
