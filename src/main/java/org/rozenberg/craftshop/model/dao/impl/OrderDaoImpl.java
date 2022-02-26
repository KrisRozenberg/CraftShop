package org.rozenberg.craftshop.model.dao.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rozenberg.craftshop.exception.DaoException;
import org.rozenberg.craftshop.model.dao.OrderDao;
import org.rozenberg.craftshop.model.entity.Order;
import org.rozenberg.craftshop.model.entity.OrderStatus;
import org.rozenberg.craftshop.model.pool.CustomConnectionPool;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDaoImpl implements OrderDao {
    private static final Logger logger = LogManager.getLogger();
    private static final int ONE_CONSTANT = 1;

    private static final String CREATE_QUERY = """
            INSERT INTO orders (date, price, address, status, user_id) 
            VALUES (?, ?, ?, ?, ?);""";

    private static final String GET_ALL_QUERY = """
            SELECT order_id, date, price, address, status, user_id   
            FROM orders;""";

    private static final String GET_ALL_FOR_USER_QUERY = """
            SELECT order_id, date, price, address, status, user_id   
            FROM orders  
            WHERE user_id = ? AND status IN('ACTIVE', 'DONE');""";

    private static final String GET_BY_ID_QUERY = """
            SELECT order_id, date, price, address, status, user_id   
            FROM orders  
            WHERE order_id = ?;""";

    private static final String GET_PRICE_QUERY = """
            SELECT price  
            FROM orders 
            WHERE order_id = ?;""";

    private static final String SET_PRICE_QUERY = """
            UPDATE orders  
            SET price = ? 
            WHERE order_id = ?;""";

    private static final String REJECT_BY_ID_QUERY = """
            UPDATE orders
            SET status = 'REJECTED' 
            WHERE order_id = ?;""";

    private static final String COMPLETE_BY_ID_QUERY = """
            UPDATE orders
            SET status = 'DONE' 
            WHERE order_id = ?;""";

    private static final String GET_STATUS_BY_ID_QUERY = """
            SELECT status
            FROM orders
            WHERE order_id = ?;""";

    private static OrderDaoImpl instance;

    private OrderDaoImpl() {
    }

    public static OrderDao getInstance() {
        if (instance == null) {
            instance = new OrderDaoImpl();
        }
        return instance;
    }

    @Override
    public Order create(Order order) throws DaoException {
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_QUERY,
                     Statement.RETURN_GENERATED_KEYS)) {
            statement.setDate(1, Date.valueOf(order.getDate()));
            statement.setBigDecimal(2, order.getPrice());
            statement.setString(3, order.getAddress());
            statement.setString(4, order.getStatus().getDbValue());
            statement.setLong(5, order.getUserId());
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    long orderId = resultSet.getLong(1);
                    order.setOrderId(orderId);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to create order: ", e);
        }
        logger.log(Level.DEBUG, "Order was created: {}", order);
        return order;
    }

    @Override
    public List<Order> getAll() throws DaoException {
        List<Order> allOrders = new ArrayList<>();
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Order order = extractOrder(resultSet);
                allOrders.add(order);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find all orders: ", e);
        }

        logger.log(Level.DEBUG, "All orders: {}", allOrders);
        return allOrders;
    }

    @Override
    public List<Order> getAllForUser(long userId) throws DaoException {
        List<Order> userOrders = new ArrayList<>();
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_FOR_USER_QUERY)) {
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = extractOrder(resultSet);
                    userOrders.add(order);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find all user's orders by user id: ", e);
        }

        logger.log(Level.DEBUG, "User's orders: {}", userOrders);
        return userOrders;
    }

    @Override
    public Optional<Order> getById(long id) throws DaoException {
        Optional<Order> order = Optional.empty();
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BY_ID_QUERY)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    order = Optional.of(extractOrder(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find order by id: ", e);
        }
        logger.log(Level.DEBUG, "Found order by id {}: {}", id, order.orElse(new Order()));
        return order;
    }

    @Override
    public BigDecimal getPrice(long id) throws DaoException{
        BigDecimal price = BigDecimal.ZERO;
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PRICE_QUERY)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    price = resultSet.getBigDecimal(1);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to get order's price: ", e);
        }
        logger.log(Level.DEBUG, "Order's price by id {}: {}", id, price);
        return price;
    }

    @Override
    public boolean setPrice(long id, BigDecimal price) throws DaoException {
        boolean isUpdated;
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SET_PRICE_QUERY)) {
            statement.setBigDecimal(1, price);
            statement.setLong(2, id);
            isUpdated = statement.executeUpdate() == ONE_CONSTANT;
        } catch (SQLException e) {
            throw new DaoException("Failed to update price: ", e);
        }
        logger.log(Level.DEBUG, "Price was updated: {}", isUpdated);
        return isUpdated;
    }

    @Override
    public boolean rejectById(long id) throws DaoException {
        boolean isRejected;
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(REJECT_BY_ID_QUERY)) {
            statement.setLong(1, id);
            isRejected = statement.executeUpdate() == ONE_CONSTANT;
        } catch (SQLException e) {
            throw new DaoException("Failed to reject order by id: ", e);
        }
        logger.log(Level.DEBUG, "Row was updated: {}", isRejected);
        return isRejected;
    }

    @Override
    public boolean completeById(long id) throws DaoException {
        boolean isCompleted;
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(COMPLETE_BY_ID_QUERY)) {
            statement.setLong(1, id);
            isCompleted = statement.executeUpdate() == ONE_CONSTANT;
        } catch (SQLException e) {
            throw new DaoException("Failed to complete order by id: ", e);
        }
        logger.log(Level.DEBUG, "Row was updated: {}", isCompleted);
        return isCompleted;
    }

    @Override
    public Optional<OrderStatus> getStatusById(long id) throws DaoException {
        Optional<OrderStatus> status = Optional.empty();
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_STATUS_BY_ID_QUERY)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    status = Optional.of(
                            OrderStatus.valueOf(
                                    resultSet.getString(5).toUpperCase().replaceAll(" ", "_")));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to get status by id: ", e);
        }
        logger.log(Level.DEBUG, "Found status by id {}: {}", id, status.orElse(null));
        return status;
    }

    private Order extractOrder(ResultSet resultSet) throws SQLException{
        return new Order(
                resultSet.getLong(1),
                resultSet.getDate(2).toLocalDate(),
                resultSet.getBigDecimal(3),
                resultSet.getString(4),
                OrderStatus.valueOf(resultSet.getString(5).toUpperCase().replaceAll(" ", "_")),
                resultSet.getLong(6));
    }
}
