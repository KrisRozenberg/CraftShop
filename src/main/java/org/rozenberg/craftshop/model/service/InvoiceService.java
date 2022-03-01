package org.rozenberg.craftshop.model.service;

import org.rozenberg.craftshop.exception.DaoException;
import org.rozenberg.craftshop.exception.ServiceException;
import org.rozenberg.craftshop.model.entity.Invoice;

import java.math.BigDecimal;
import java.util.Optional;

public interface InvoiceService {
    Invoice create(Invoice invoice) throws ServiceException;
    Optional<BigDecimal> getMoney(long id) throws ServiceException;
    boolean setMoney(long id, BigDecimal money) throws ServiceException;
    Optional<BigDecimal> getDiscount(long id) throws ServiceException;
    boolean setDiscount(long id, BigDecimal discount) throws ServiceException;
}
