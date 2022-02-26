package org.rozenberg.craftshop.model.dao.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rozenberg.craftshop.exception.DaoException;
import org.rozenberg.craftshop.model.dao.CategoryDao;
import org.rozenberg.craftshop.model.entity.Category;
import org.rozenberg.craftshop.model.pool.CustomConnectionPool;

import java.sql.*;
import java.util.Optional;

public class CategoryDaoImpl implements CategoryDao {
    private static final Logger logger = LogManager.getLogger();

    private static final String CREATE_QUERY = """
            INSERT INTO categories (name)
            VALUES (?);""";

    private static final String GET_BY_ID_QUERY = """
            SELECT category_id, name
            FROM categories
            WHERE category_id = ?;""";

    private static CategoryDaoImpl instance;

    private CategoryDaoImpl() {
    }

    public static CategoryDaoImpl getInstance() {
        if (instance == null) {
            instance = new CategoryDaoImpl();
        }
        return instance;
    }

    @Override
    public Category create(Category category) throws DaoException {
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_QUERY,
                     Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, category.getName());
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    long categoryId = resultSet.getLong(1);
                    category.setCategoryId(categoryId);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Creating category query failed: ", e);
        }
        logger.log(Level.DEBUG, "New category is created: {}", category);
        return category;
    }

    @Override
    public Optional<Category> getById(long id) throws DaoException {
        Optional<Category> category = Optional.empty();
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BY_ID_QUERY)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    category = Optional.of(new Category(resultSet.getLong(1), resultSet.getString(2)));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to get category: ", e);
        }
        logger.log(Level.DEBUG, "Found category: {}", category.orElse(new Category()));
        return category;
    }
}
