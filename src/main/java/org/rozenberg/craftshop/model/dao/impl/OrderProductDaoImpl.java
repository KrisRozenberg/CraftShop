package org.rozenberg.craftshop.model.dao.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rozenberg.craftshop.exception.DaoException;
import org.rozenberg.craftshop.model.dao.OrderProductDao;
import org.rozenberg.craftshop.model.pool.CustomConnectionPool;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderProductDaoImpl implements OrderProductDao {
    private static final Logger logger = LogManager.getLogger();

    private static final String CALCULATE_ORDER_REAL_PRICE_QUERY = """
            SELECT SUM(h.quantity * p.price) AS order_real_price
            FROM orders_has_products h, products p
            WHERE h.product_id = p.product_id AND h.order_id = ?;""";

    private static OrderProductDaoImpl instance;

    private OrderProductDaoImpl() {
    }

    public static OrderProductDaoImpl getInstance() {
        if (instance == null) {
            instance = new OrderProductDaoImpl();
        }
        return instance;
    }

    @Override
    public BigDecimal calculateOrderRealPrice(long id) throws DaoException{
        BigDecimal price = BigDecimal.ZERO;
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(CALCULATE_ORDER_REAL_PRICE_QUERY)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    price = resultSet.getBigDecimal(1);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to calculate order's real price: ", e);
        }
        logger.log(Level.DEBUG, "Found order's real price by id {}: {}", id, price);
        return price;
    }
}
