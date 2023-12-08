package com.vstu.internetshop.entity;

import com.vstu.internetshop.dto.ProductStatus;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductEntity {
    private Integer id;
    private String name;
    private BigDecimal price;
    private String description;
    private UserEntity createdUser;
    private ProductStatus status;
    private OrderEntity order;

    public ProductEntity(Integer id, String name, BigDecimal price, String description, UserEntity createdUser, ProductStatus status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.createdUser = createdUser;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserEntity getCreatedUser() {
        return createdUser;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedUser(UserEntity createdUser) {
        this.createdUser = createdUser;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEntity that = (ProductEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
