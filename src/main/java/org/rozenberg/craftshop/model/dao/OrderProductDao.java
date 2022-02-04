package org.rozenberg.craftshop.model.dao;

import org.rozenberg.craftshop.exception.DaoException;

import java.math.BigDecimal;

public interface OrderProductDao {
    BigDecimal calculateOrderRealPrice(long id) throws DaoException;
}
