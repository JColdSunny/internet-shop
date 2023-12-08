package com.vstu.internetshop.dto;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum ProductStatus {
    AVAILABLE("AVAILABLE"),
    IN_ORDER("IN_ORDER"),
    PAID("PAID");

    private static final Map<String, ProductStatus> STRING_TO_ENUM = Stream.of(values())
            .collect(toMap(Objects::toString, Function.identity()));

    private final String status;

    ProductStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }

    public static Optional<ProductStatus> fromString(String status) {
        return Optional.ofNullable(STRING_TO_ENUM.get(status));
    }

}
