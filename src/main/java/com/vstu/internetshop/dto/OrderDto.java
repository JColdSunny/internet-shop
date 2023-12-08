package com.vstu.internetshop.dto;

public class OrderDto {
    private final Integer id;
    private final String status;
    private final Integer userId;
    private final String username;
    private final String createdAt;

    public OrderDto(Integer id, String status, Integer userId, String username, String createdAt) {
        this.id = id;
        this.status = status;
        this.userId = userId;
        this.username = username;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}