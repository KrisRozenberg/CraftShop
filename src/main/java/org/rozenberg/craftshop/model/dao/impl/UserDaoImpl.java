package org.rozenberg.craftshop.model.dao.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rozenberg.craftshop.exception.DaoException;
import org.rozenberg.craftshop.model.dao.UserDao;
import org.rozenberg.craftshop.model.entity.Role;
import org.rozenberg.craftshop.model.entity.User;
import org.rozenberg.craftshop.model.entity.UserStatus;
import org.rozenberg.craftshop.model.pool.CustomConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = LogManager.getLogger();

    private static final String GET_BY_ID_QUERY = """
            SELECT user_id, name, surname, login, password, email, role, status, invoice_id 
            FROM users 
            WHERE user_id = ?;""";

    private static final String GET_BY_LOGIN_QUERY = """
            SELECT user_id, name, surname, login, password, email, role, status, invoice_id
            FROM users
            WHERE login = ?;""";

    private static final String GET_BY_EMAIL_QUERY = """
            SELECT user_id, name, surname, login, password, email, role, status, invoice_id
            FROM users
            WHERE email = ?;""";

    private static final String GET_ALL_QUERY = """
            SELECT user_id, name, surname, login, password, email, role, status, invoice_id
            FROM users;""";

    private static final String CREATE_ADMIN_QUERY = """
            INSERT INTO users (name, surname, login, password, email, role, status, invoice_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?);""";

    private static final String UPDATE_BY_ID_QUERY = """
            UPDATE users
            SET name = ?, surname = ?, login = ?, password = ?, email = ?, role = ?, status = ?, invoice_id = ?
            WHERE user_id = ?;""";

    private static final String BLOCK_BY_ID_QUERY = """
            UPDATE users
            SET status = 'blocked'
            WHERE user_id = ?;""";

    private static final String DELETE_BY_ID_QUERY = """
            UPDATE users
            SET status = 'inactive'
            WHERE user_id = ?;""";

    private static final String RESTORE_BY_ID_QUERY = """
            UPDATE users
            SET status = 'active'
            WHERE user_id = ?;""";

    private static final String UPDATE_PASSWORD_QUERY = """
            UPDATE users
            SET password = ?
            WHERE user_id = ?;""";

    private static UserDaoImpl instance;

    private UserDaoImpl() {
    }

    public static UserDao getInstance() {
        if (instance == null) {
            instance = new UserDaoImpl();
        }
        return instance;
    }

    @Override
    public User getById(Long id) throws DaoException {
        User user = null;
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BY_ID_QUERY)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = extractUser(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find user: ", e);
        }
        logger.log(Level.DEBUG, "Found user: {}", user);
        return user;
    }

    @Override
    public User getByLogin(String login) throws DaoException {
        User user = null;
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BY_LOGIN_QUERY)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = extractUser(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find user by login: ", e);
        }
        logger.log(Level.DEBUG, "Found user: {}", user);
        return user;
    }

    @Override
    public User getByEmail(String email) throws DaoException {
        User user = null;
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BY_EMAIL_QUERY)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = extractUser(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find user: ", e);
        }
        logger.log(Level.DEBUG, "Found user: {}", user);
        return user;
    }

    @Override
    public List<User> getAll() throws DaoException {
        List<User> allUsers = new ArrayList<>();
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = extractUser(resultSet);
                allUsers.add(user);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find all users: ", e);
        }

        logger.log(Level.DEBUG, "All users: {}", allUsers);
        return allUsers;
    }

    @Override
    public User createAdmin(User user) throws DaoException {
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_ADMIN_QUERY,
                     Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getLogin());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getRole().toString().toLowerCase());
            statement.setString(7, user.getUserStatus().toString().toLowerCase());
            statement.setLong(8, user.getInvoiceId());

            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    long userId = resultSet.getLong(1);
                    user.setUserId(userId);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to create admin: ", e);
        }
        logger.log(Level.DEBUG, "New admin was created: {}", user);
        return user;
    }

    @Override
    public User updateById(User user) throws DaoException {
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_BY_ID_QUERY)) {

            statement.setString(3, user.getName());
            statement.setString(4, user.getSurname());
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getRole().toString().toLowerCase());
            statement.setString(7, user.getUserStatus().toString().toLowerCase());
            statement.setLong(8, user.getInvoiceId());
            statement.setLong(9, user.getUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Failed to update user: ", e);
        }

        logger.log(Level.DEBUG, "User updated: {}", user);
        return user;
    }

    @Override
    public int blockById(long id) throws DaoException {
        int rowsUpdated;
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(BLOCK_BY_ID_QUERY)) {
            statement.setLong(1, id);
            rowsUpdated = statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Failed to block user by id " + id + " : ", e);
        }
        logger.log(Level.DEBUG, "Number of rows updated: {}", rowsUpdated);
        return rowsUpdated;
    }

    @Override
    public int deleteById(long id) throws DaoException {
        int rowsUpdated;
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID_QUERY)) {
            statement.setLong(1, id);
            rowsUpdated = statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Failed to delete user by id " + id + " : ", e);
        }
        logger.log(Level.DEBUG, "Number of rows updated: {}", rowsUpdated);
        return rowsUpdated;
    }

    @Override
    public int restoreById(long id) throws DaoException {
        int rowsUpdated;
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(RESTORE_BY_ID_QUERY)) {
            statement.setLong(1, id);
            rowsUpdated = statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Failed to restore user by id " + id + " : ", e);
        }
        logger.log(Level.DEBUG, "Number of rows updated: {}", rowsUpdated);
        return rowsUpdated;
    }

    @Override
    public int updatePassword(long id, String password) throws DaoException {
        int rowsUpdated;
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PASSWORD_QUERY)) {
            statement.setString(1, password);
            statement.setLong(2, id);
            rowsUpdated = statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Failed to update user's password: ", e);
        }
        logger.log(Level.DEBUG, "Number of rows updated: {}", rowsUpdated);
        return rowsUpdated;
    }

    private User extractUser(ResultSet resultSet) throws SQLException{
        return new User(
                resultSet.getLong(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getString(4),
                resultSet.getString(5),
                resultSet.getString(6),
                Role.valueOf(resultSet.getString(7).toUpperCase()),
                UserStatus.valueOf(resultSet.getString(8).toUpperCase()),
                resultSet.getLong(9));
    }
}
