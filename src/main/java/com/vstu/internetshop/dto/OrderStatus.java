package com.vstu.internetshop.dto;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum OrderStatus {
    CREATED("CREATED"),
    PROCESSING("PROCESSING"),
    CONFIRMED("CONFIRMED"),
    CANCELED("CANCELED"),
    IN_DELIVERY("IN_DELIVERY"),
    DELIVERED("DELIVERED");

    private static final Map<String, OrderStatus> STRING_TO_ENUM = Stream.of(values())
            .collect(toMap(Objects::toString, Function.identity()));

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }

    public static Optional<OrderStatus> fromString(String status) {
        return Optional.ofNullable(STRING_TO_ENUM.get(status));
    }
}
