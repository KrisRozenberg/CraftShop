package org.rozenberg.craftshop.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Order {
    private long orderId;
    private LocalDate date;
    private BigDecimal price;
    private String address;
    private OrderStatus status;
    private long userId;

    public Order(){}

    public Order(long orderId, LocalDate date, BigDecimal price, String address, OrderStatus status, long userId) {
        this.orderId = orderId;
        this.date = date;
        this.price = price;
        this.address = address;
        this.status = status;
        this.userId = userId;
    }

    public Order(LocalDate date, BigDecimal price, String address, OrderStatus status, long userId) {
        this.date = date;
        this.price = price;
        this.address = address;
        this.status = status;
        this.userId = userId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Order newOrder = (Order) obj;
        if (orderId != newOrder.orderId) return false;
        if (date != null ? !date.equals(newOrder.date) : newOrder.date != null) return false;
        if (price.compareTo(newOrder.price) != 0) return false;
        if (address != null ? !address.equals(newOrder.address) : newOrder.address != null) return false;
        if (status != newOrder.status) return false;
        return userId == newOrder.userId;
    }

    @Override
    public int hashCode() {
        int result = (int) (orderId ^ (orderId >>> 32));
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + price.intValue();
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("orderId=").append(orderId);
        sb.append(", date='").append(date).append('\'');
        sb.append(", price=").append(price);
        sb.append(", address='").append(address).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append(", userId=").append(userId);
        sb.append('}');
        return sb.toString();
    }
}
