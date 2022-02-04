package org.rozenberg.craftshop.model.entity;

public enum OrderStatus {
    IN_PROCESS("in process"),
    DONE("done"),
    REJECTED("rejected");

    private final String dbValue;

    OrderStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }
}
