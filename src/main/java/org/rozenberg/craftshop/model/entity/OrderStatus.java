package org.rozenberg.craftshop.model.entity;

public enum OrderStatus {
    IN_PROCESS("in process"),
    DONE("done"),
    REJECTED("rejected");

    private final String bdValue;

    OrderStatus(String bdValue) {
        this.bdValue = bdValue;
    }

    public String getBdValue() {
        return bdValue;
    }
}
