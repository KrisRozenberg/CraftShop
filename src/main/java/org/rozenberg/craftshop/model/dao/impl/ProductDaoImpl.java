package org.rozenberg.craftshop.model.dao.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rozenberg.craftshop.exception.DaoException;
import org.rozenberg.craftshop.model.dao.ProductDao;
import org.rozenberg.craftshop.model.entity.*;
import org.rozenberg.craftshop.model.pool.CustomConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDaoImpl implements ProductDao {
    private static final Logger logger = LogManager.getLogger();
    private static final int ONE_CONSTANT = 1;

    private static final String CREATE_QUERY = """
            INSERT INTO products (name, description, image_url, price, status, category_id) 
            VALUES (?, ?, ?, ?, ?, ?);""";

    private static final String UPDATE_BY_ID_QUERY = """
            UPDATE products 
            SET name = ?, description = ?, image_url = ?, price = ?, status = ?, category_id = ? 
            WHERE product_id = ?;""";

    private static final String DELETE_BY_ID_QUERY = """
            UPDATE products 
            SET status = 'out of stock' 
            WHERE product_id = ?;""";

    private static final String GET_BY_ID_QUERY = """
            SELECT product_id, name, description, image_url, price, status, category_id
            FROM products 
            WHERE product_id = ?;""";

    private static final String GET_ALL_QUERY = """
            SELECT product_id, name, description, image_url, price, status, category_id  
            FROM products;""";

    private static final String GET_BY_CATEGORY_NAME_QUERY = """
            SELECT p.product_id, p.name, p.description, p.image_url, p.price, p.status, p.category_id  
            FROM products p, categories c
            WHERE p.category_id = c.category_id AND c.name = ?;""";

    private static final String GET_ALL_IN_STOCK_QUERY = """
            SELECT product_id, name, description, image_url, price, status, category_id  
            FROM products
            WHERE status = 'in stock';""";

    private static final String RESTORE_BY_ID_QUERY = """
            UPDATE products 
            SET status = 'in stock' 
            WHERE product_id = ?;""";

    private static ProductDaoImpl instance;

    private ProductDaoImpl() {
    }

    public static ProductDao getInstance() {
        if (instance == null) {
            instance = new ProductDaoImpl();
        }
        return instance;
    }

    @Override
    public Product create(Product product) throws DaoException {
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_QUERY,
                     Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setString(3, product.getImageUrl());
            statement.setBigDecimal(4, product.getPrice());
            statement.setString(5, product.getStatus().getDbValue());
            statement.setLong(6, product.getCategoryId());
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    long productId = resultSet.getLong(1);
                    product.setProductId(productId);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to create product: ", e);
        }
        logger.log(Level.DEBUG, "Product was created: {}", product);
        return product;
    }

    @Override
    public Product updateById(Product product) throws DaoException {
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_BY_ID_QUERY)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setString(3, product.getImageUrl());
            statement.setBigDecimal(4, product.getPrice());
            statement.setString(5, product.getStatus().getDbValue());
            statement.setLong(6, product.getCategoryId());
            statement.setLong(7, product.getProductId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Failed to update product: ", e);
        }
        logger.log(Level.DEBUG, "Product was updated: {}", product);
        return product;
    }

    @Override
    public boolean deleteById(long id) throws DaoException {
        boolean isDeleted;
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID_QUERY)) {
            statement.setLong(1, id);
            isDeleted = statement.executeUpdate() == ONE_CONSTANT;
        } catch (SQLException e) {
            throw new DaoException("Failed to delete product by id " + id + " : ", e);
        }
        logger.log(Level.DEBUG, "Row was updated: {}", isDeleted);
        return isDeleted;
    }

    @Override
    public Optional<Product> getById(long id) throws DaoException {
       Optional<Product> product = Optional.empty();
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BY_ID_QUERY)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    product = Optional.of(extractProduct(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find product by id: ", e);
        }
        logger.log(Level.DEBUG, "Found product by id {}: {}", id, product.orElse(new Product()));
        return product;
    }

    @Override
    public List<Product> getAll() throws DaoException {
        List<Product> allProducts = new ArrayList<>();
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Product product = extractProduct(resultSet);
                allProducts.add(product);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find all products: ", e);
        }
        logger.log(Level.DEBUG, "All products: {}", allProducts);
        return allProducts;
    }

    @Override
    public List<Product> getByCategoryName(String name) throws DaoException {
        List<Product> productsByCategory = new ArrayList<>();
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BY_CATEGORY_NAME_QUERY)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = extractProduct(resultSet);
                    productsByCategory.add(product);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find products by category: ", e);
        }
        logger.log(Level.DEBUG, "Products by provided category: {}", productsByCategory);
        return productsByCategory;
    }

    @Override
    public List<Product> getAllInStock(String name) throws DaoException {
        List<Product> productsInStock = new ArrayList<>();
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_IN_STOCK_QUERY);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Product product = extractProduct(resultSet);
                productsInStock.add(product);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find products in stock: ", e);
        }
        logger.log(Level.DEBUG, "Products in stock: {}", productsInStock);
        return productsInStock;
    }

    @Override
    public boolean restoreById(long id) throws DaoException {
        boolean isRestored;
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(RESTORE_BY_ID_QUERY)) {
            statement.setLong(1, id);
            isRestored = statement.executeUpdate() == ONE_CONSTANT;
        } catch (SQLException e) {
            throw new DaoException("Failed to restore product by id " + id + " : ", e);
        }
        logger.log(Level.DEBUG, "Row was updated: {}", isRestored);
        return isRestored;
    }

    private Product extractProduct(ResultSet resultSet) throws SQLException{
        return new Product(
                resultSet.getLong(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getString(4),
                resultSet.getBigDecimal(5),
                ProductStatus.valueOf(resultSet.getString(6).toUpperCase().replaceAll(" ", "_")),
                resultSet.getLong(7));
    }
}
