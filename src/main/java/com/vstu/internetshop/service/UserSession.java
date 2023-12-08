package com.vstu.internetshop.service;

import com.vstu.internetshop.entity.OrderEntity;
import com.vstu.internetshop.entity.UserEntity;

public final class UserSession {
    private UserEntity user;
    private OrderEntity order;

    public static final UserSession SESSION = new UserSession();

    private UserSession() {
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

}
