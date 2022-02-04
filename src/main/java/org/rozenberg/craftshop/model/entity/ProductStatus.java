package org.rozenberg.craftshop.model.entity;

public enum ProductStatus {
    IN_STOCK("in stock"),
    OUT_OF_STOCK("out of stock");

    private final String dbValue;

    ProductStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }
}
