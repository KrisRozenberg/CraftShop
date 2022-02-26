package org.rozenberg.craftshop.model.dao;

import org.rozenberg.craftshop.exception.DaoException;
import org.rozenberg.craftshop.model.entity.Order;
import org.rozenberg.craftshop.model.entity.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderDao {
    Order create(Order order) throws DaoException;
    List<Order> getAll() throws DaoException;
    List<Order> getAllForUser(long clientId) throws DaoException;
    Optional<Order> getById(long id) throws DaoException;
    BigDecimal getPrice(long id) throws DaoException;
    boolean setPrice(long id, BigDecimal price) throws DaoException;
    boolean rejectById(long id) throws DaoException;
    boolean completeById(long id) throws DaoException;
    Optional<OrderStatus> getStatusById(long id) throws DaoException;
}
