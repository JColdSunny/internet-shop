package com.vstu.internetshop.entity;

import com.vstu.internetshop.dto.OrderStatus;

import java.time.LocalDateTime;
import java.util.Objects;

public class OrderEntity {
    private int id;
    private UserEntity user;
    private OrderStatus status;
    private LocalDateTime createdAt;

    public OrderEntity(int id, UserEntity user, OrderStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity that = (OrderEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
