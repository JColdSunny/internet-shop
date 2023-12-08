package com.vstu.internetshop.dto;

import java.math.BigDecimal;
import java.util.Objects;

public final class ProductDto {
    Integer id;
    String name;
    BigDecimal price;
    String description;
    String username;
    String status;

    public ProductDto(Integer id, String name, BigDecimal price, String description, String username, String status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.username = username;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDto that = (ProductDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
