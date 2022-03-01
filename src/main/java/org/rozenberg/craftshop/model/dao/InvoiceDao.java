package org.rozenberg.craftshop.model.dao;

import org.rozenberg.craftshop.exception.DaoException;
import org.rozenberg.craftshop.model.entity.Invoice;

import java.math.BigDecimal;
import java.util.Optional;

public interface InvoiceDao {
    Invoice create() throws DaoException;
    Optional<BigDecimal> getMoney(long id) throws DaoException;
    boolean setMoney(long id, BigDecimal money) throws DaoException;
    Optional<BigDecimal> getDiscount(long id) throws DaoException;
    boolean setDiscount(long id, BigDecimal discount) throws DaoException;
}
