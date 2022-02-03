package org.rozenberg.craftshop.model.entity;

public enum ProductStatus {
    IN_STOCK("in stock"),
    OUT_OF_STOCK("out of stock");

    private final String bdValue;

    ProductStatus(String bdValue) {
        this.bdValue = bdValue;
    }

    public String getBdValue() {
        return bdValue;
    }
}
