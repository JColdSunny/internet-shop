package com.vstu.internetshop.service;

import com.vstu.internetshop.entity.OrderEntity;
import com.vstu.internetshop.entity.UserEntity;

/**
 * Этот класс предоставляет методы для управления сессией пользователя.
 */
public final class UserSession {
    private UserEntity user;
    private OrderEntity order;


    /**
     * Всегда возвращает один и тот же экземпляр сессии.
     */
    public static final UserSession SESSION = new UserSession();

    private UserSession() {
    }

    /**
     * Получает экземпляр {@link UserEntity} текущего пользователя сессии.
     *
     * @return экземпляр {@link UserEntity} текущего пользователя.
     */
    public UserEntity getUser() {
        return user;
    }

    /**
     * Устанавливает нового пользователя для этой сессии.
     *
     * @param user новый пользователь для этой сессии.
     */
    public void setUser(UserEntity user) {
        this.user = user;
    }

    /**
     * Получает экземпляр {@link OrderEntity} текущего заказа в сессии.
     *
     * @return экземпляр {@link OrderEntity} текущего заказа.
     */
    public OrderEntity getOrder() {
        return order;
    }

    /**
     * Устанавливает новый заказ для этой сессии.
     *
     * @param order новый заказ для этой сессии.
     */
    public void setOrder(OrderEntity order) {
        this.order = order;
    }

}