package org.rozenberg.craftshop.model.entity;

import java.math.BigDecimal;

public class Product {
    private long productId;
    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal price;
    private ProductStatus status;
    private long categoryId;

    public Product(){}

    public Product(long productId, String name, String description, String imageUrl, BigDecimal price, ProductStatus status, long categoryId) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.status = status;
        this.categoryId = categoryId;
    }

    public Product( String name, String description, String imageUrl, BigDecimal price, ProductStatus status, long categoryId) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.status = status;
        this.categoryId = categoryId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product newProduct = (Product) obj;
        if (productId != newProduct.productId) return false;
        if (name != null ? !name.equals(newProduct.name) : newProduct.name != null) return false;
        if (description != null ? !description.equals(newProduct.description) : newProduct.description != null) return false;
        if (imageUrl != null ? !imageUrl.equals(newProduct.imageUrl) : newProduct.imageUrl != null) return false;
        if (price.compareTo(newProduct.price) != 0) return false;
        if (status != newProduct.status) return false;
        return categoryId == newProduct.categoryId;
    }

    @Override
    public int hashCode() {
        int result = (int) (productId ^ (productId >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + price.intValue();
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (int) (categoryId ^ (categoryId >>> 32));
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Product{");
        sb.append("productId=").append(productId);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", imageUrl='").append(imageUrl).append('\'');
        sb.append(", price=").append(price);
        sb.append(", status='").append(status).append('\'');
        sb.append(", categoryId=").append(categoryId);
        sb.append('}');
        return sb.toString();
    }
}
