package org.rozenberg.craftshop.model.service.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rozenberg.craftshop.exception.DaoException;
import org.rozenberg.craftshop.exception.ServiceException;
import org.rozenberg.craftshop.model.dao.InvoiceDao;
import org.rozenberg.craftshop.model.dao.impl.InvoiceDaoImpl;
import org.rozenberg.craftshop.model.entity.Invoice;
import org.rozenberg.craftshop.model.service.InvoiceService;

import java.math.BigDecimal;
import java.util.Optional;

public class InvoiceServiceImpl implements InvoiceService {
    private static final Logger logger = LogManager.getLogger();
    private static InvoiceServiceImpl instance;
    private final InvoiceDao invoiceDao = InvoiceDaoImpl.getInstance();

    private InvoiceServiceImpl() {
    }

    public static InvoiceServiceImpl getInstance() {
        if (instance == null) {
            instance = new InvoiceServiceImpl();
        }
        return instance;
    }

    @Override
    public Invoice create(Invoice invoice) throws ServiceException {
        Invoice newInvoice;
        try {
            newInvoice = invoiceDao.create();
        } catch (DaoException e) {
            throw new ServiceException("Failed to create invoice: ", e);
        }
        logger.log(Level.DEBUG, "Created invoice: {}", newInvoice);
        return newInvoice;
    }

    @Override
    public Optional<BigDecimal> getMoney(long id) throws ServiceException {
        Optional<BigDecimal> money;
        try {
            money = invoiceDao.getMoney(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to find money by id: ", e);
        }
        logger.log(Level.DEBUG, "Found money: {}", money);
        return money;
    }

    @Override
    public boolean setMoney(long id, BigDecimal money) throws ServiceException {
        return false;
    }

    @Override
    public Optional<BigDecimal> getDiscount(long id) throws ServiceException {
        Optional<BigDecimal> discount;
        try {
            discount = invoiceDao.getDiscount(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to find discount by id: ", e);
        }
        logger.log(Level.DEBUG, "Found discount: {}", discount);
        return discount;
    }

    @Override
    public boolean setDiscount(long id, BigDecimal discount) throws ServiceException {
        return false;
    }
}
