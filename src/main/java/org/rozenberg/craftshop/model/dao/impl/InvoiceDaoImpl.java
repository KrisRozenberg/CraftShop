package org.rozenberg.craftshop.model.dao.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rozenberg.craftshop.exception.DaoException;
import org.rozenberg.craftshop.model.dao.InvoiceDao;
import org.rozenberg.craftshop.model.entity.Invoice;
import org.rozenberg.craftshop.model.pool.CustomConnectionPool;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Optional;

public class InvoiceDaoImpl implements InvoiceDao {
    private static final Logger logger = LogManager.getLogger();
    private static final int ONE_CONSTANT = 1;

    private static final String CREATE_QUERY = """
            INSERT INTO invoices (money, discount) 
            VALUES (?, ?);""";

    private static final String GET_MONEY_QUERY = """
            SELECT money  
            FROM invoices 
            WHERE invoice_id = ?;""";

    private static final String SET_MONEY_QUERY = """
            UPDATE invoices  
            SET money = ? 
            WHERE invoice_id = ?;""";

    private static final String GET_DISCOUNT_QUERY = """
            SELECT discount 
            FROM invoices 
            WHERE invoice_id = ?;""";

    private static final String SET_DISCOUNT_QUERY = """
            UPDATE invoices  
            SET discount = ? 
            WHERE invoice_id = ?;""";

    private static InvoiceDaoImpl instance;

    private InvoiceDaoImpl() {
    }

    public static InvoiceDao getInstance() {
        if (instance == null) {
            instance = new InvoiceDaoImpl();
        }
        return instance;
    }

    @Override
    public Invoice create(Invoice invoice) throws DaoException {
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_QUERY,
                     Statement.RETURN_GENERATED_KEYS)) {
            statement.setBigDecimal(1, invoice.getMoney());
            statement.setBigDecimal(2, invoice.getDiscount());
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    long invoiceId = resultSet.getLong(1);
                    invoice.setInvoiceId(invoiceId);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to create invoice: ", e);
        }
        logger.log(Level.DEBUG, "Invoice was created: {}", invoice);
        return invoice;
    }

    @Override
    public Optional<BigDecimal> getMoney(long id) throws DaoException {
        Optional<BigDecimal> money = Optional.empty();
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_MONEY_QUERY)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    money = Optional.of(resultSet.getBigDecimal(1));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to get money by id: ", e);
        }
        logger.log(Level.DEBUG, "Found money by id {}: {}", id, money.orElse(BigDecimal.valueOf(-1)));
        return money;
    }

    @Override
    public boolean setMoney(long id, BigDecimal money) throws DaoException {
        boolean isUpdated;
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SET_MONEY_QUERY)) {
            statement.setBigDecimal(1, money);
            statement.setLong(2, id);
            isUpdated = statement.executeUpdate() == ONE_CONSTANT;
        } catch (SQLException e) {
            throw new DaoException("Failed to update money: ", e);
        }
        logger.log(Level.DEBUG, "Money was updated: {}", isUpdated);
        return isUpdated;
    }

    @Override
    public Optional<BigDecimal> getDiscount(long id) throws DaoException {
        Optional<BigDecimal> discount = Optional.empty();
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_DISCOUNT_QUERY)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    discount = Optional.of(resultSet.getBigDecimal(1));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to get discount by id: ", e);
        }
        logger.log(Level.DEBUG, "Found discount by id {}: {}", id, discount.orElse(BigDecimal.valueOf(-1)));
        return discount;
    }

    @Override
    public boolean setDiscount(long id, BigDecimal discount) throws DaoException {
        boolean isUpdated;
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SET_DISCOUNT_QUERY)) {
            statement.setBigDecimal(1, discount);
            statement.setLong(2, id);
            isUpdated = statement.executeUpdate() == ONE_CONSTANT;
        } catch (SQLException e) {
            throw new DaoException("Failed to update discount: ", e);
        }
        logger.log(Level.DEBUG, "Discount was updated: {}", isUpdated);
        return isUpdated;
    }
}
