package com.vstu.internetshop.dto;

import java.math.BigDecimal;

public final class OrderHistoryDto {
    private final int count;
    private final BigDecimal price;
    private final String status;

    public OrderHistoryDto(int count, BigDecimal price, String status) {
        this.count = count;
        this.price = price;
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }
}
