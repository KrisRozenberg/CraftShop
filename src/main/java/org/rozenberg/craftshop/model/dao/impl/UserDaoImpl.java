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
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = LogManager.getLogger();
    private static final int ONE_CONSTANT = 1;

    private static final String CREATE_USER_QUERY = """
            INSERT INTO users (name, surname, login, password, email, role, status, invoice_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?);""";

    private static final String CREATE_CLIENT_INVOICE_QUERY = """
            INSERT INTO invoices (money, discount) 
            VALUES (DEFAULT, DEFAULT);""";

    private static final String GET_BY_ID_QUERY = """
            SELECT user_id, name, surname, login, password, email, role, status, invoice_id 
            FROM users 
            WHERE user_id = ?;""";

    private static final String GET_BY_LOGIN_QUERY = """
            SELECT user_id, name, surname, login, password, email, role, status, invoice_id
            FROM users
            WHERE login = ?;""";

    private static final String GET_BY_LOGIN_AND_PASSWORD_QUERY = """
            SELECT user_id, name, surname, login, password, email, role, status, invoice_id
            FROM users
            WHERE login = ? AND password = ?;""";

    private static final String GET_BY_EMAIL_QUERY = """
            SELECT user_id, name, surname, login, password, email, role, status, invoice_id
            FROM users
            WHERE email = ?;""";

    private static final String GET_ALL_QUERY = """
            SELECT user_id, name, surname, login, password, email, role, status, invoice_id
            FROM users;""";

    private static final String UPDATE_BY_ID_QUERY = """
            UPDATE users
            SET name = ?, surname = ?, login = ?, password = ?, email = ?, role = ?, status = ?, invoice_id = ?
            WHERE user_id = ?;""";

    private static final String BLOCK_BY_ID_QUERY = """
            UPDATE users
            SET status = 'blocked'
            WHERE user_id = ?;""";
//
//    private static final String DELETE_BY_ID_QUERY = """
//            UPDATE users
//            SET status = 'inactive'
//            WHERE user_id = ?;""";

    private static final String RESTORE_BY_ID_QUERY = """
            UPDATE users
            SET status = 'active'
            WHERE user_id = ?;""";

    private static final String UPDATE_PASSWORD_BY_ID_QUERY = """
            UPDATE users
            SET password = ?
            WHERE user_id = ?;""";

    private static final String GET_PASSWORD_BY_LOGIN_QUERY = """
            SELECT password
            FROM users
            WHERE login = ?;""";

    private static final String GET_PASSWORD_BY_EMAIL_QUERY = """
            SELECT password
            FROM users
            WHERE email = ?;""";

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
    public User create(User user) throws DaoException {
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement userStatement = connection.prepareStatement(CREATE_USER_QUERY,
                     Statement.RETURN_GENERATED_KEYS)) {
            if (user.getRole() == Role.CLIENT) {
                try (PreparedStatement invoiceStatement = connection.prepareStatement(CREATE_CLIENT_INVOICE_QUERY,
                        Statement.RETURN_GENERATED_KEYS)) {
                    connection.setAutoCommit(false);
                    invoiceStatement.executeUpdate();
                    try (ResultSet resultSet = invoiceStatement.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            long invoiceId = resultSet.getLong(1);
                            user.setInvoiceId(invoiceId);
                        }
                    }
                    userStatement.setString(1, user.getName());
                    userStatement.setString(2, user.getSurname());
                    userStatement.setString(3, user.getLogin());
                    userStatement.setString(4, user.getPassword());
                    userStatement.setString(5, user.getEmail());
                    userStatement.setString(6, user.getRole().toString().toLowerCase());
                    userStatement.setString(7, user.getUserStatus().toString().toLowerCase());
                    userStatement.setLong(8, user.getInvoiceId());
                    userStatement.executeUpdate();
                    try (ResultSet resultSet = userStatement.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            long userId = resultSet.getLong(1);
                            user.setUserId(userId);
                        }
                    }
                    connection.commit();
                } catch (SQLException e) {
                    connection.rollback();
                    throw new DaoException("Failed to create new client: " + user, e);
                } finally {
                    connection.setAutoCommit(true);
                }
            }
            else {
                user.setInvoiceId(1);
                userStatement.setString(1, user.getName());
                userStatement.setString(2, user.getSurname());
                userStatement.setString(3, user.getLogin());
                userStatement.setString(4, user.getPassword());
                userStatement.setString(5, user.getEmail());
                userStatement.setString(6, user.getRole().toString().toLowerCase());
                userStatement.setString(7, user.getUserStatus().toString().toLowerCase());
                userStatement.setLong(8, user.getInvoiceId());
                userStatement.executeUpdate();
                try (ResultSet resultSet = userStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        long userId = resultSet.getLong(1);
                        user.setUserId(userId);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to create user: ", e);
        }
        logger.log(Level.DEBUG, "New user was created: {}", user);
        return user;
    }

    @Override
    public Optional<User> getById(Long id) throws DaoException {
        Optional<User> user = Optional.empty();
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BY_ID_QUERY)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = Optional.of(extractUser(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find user: ", e);
        }
        logger.log(Level.DEBUG, "Found user: {}", user.orElse(new User()));
        return user;
    }

    @Override
    public Optional<User> getByLogin(String login) throws DaoException {
        Optional<User> user = Optional.empty();
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BY_LOGIN_QUERY)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = Optional.of(extractUser(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find user by login: ", e);
        }
        logger.log(Level.DEBUG, "Found user: {}", user.orElse(new User()));
        return user;
    }

    @Override
    public Optional<User> getByLoginAndPassword(String login, String password) throws DaoException {
        Optional<User> user = Optional.empty();
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BY_LOGIN_AND_PASSWORD_QUERY)) {
            statement.setString(1, login);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = Optional.of(extractUser(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find user by login and password: ", e);
        }
        logger.log(Level.DEBUG, "Found user: {}", user.orElse(new User()));
        return user;
    }

    @Override
    public Optional<User> getByEmail(String email) throws DaoException {
        Optional<User> user = Optional.empty();
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BY_EMAIL_QUERY)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = Optional.of(extractUser(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find user: ", e);
        }
        logger.log(Level.DEBUG, "Found user: {}", user.orElse(new User()));
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
    public User updateById(User user) throws DaoException {
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_BY_ID_QUERY)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getLogin());
            statement.setString(4, user.getPassword());
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
    public boolean blockById(long id) throws DaoException {
        boolean isBlocked;
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(BLOCK_BY_ID_QUERY)) {
            statement.setLong(1, id);
            isBlocked = statement.executeUpdate() == ONE_CONSTANT;
        } catch (SQLException e) {
            throw new DaoException("Failed to block user by id " + id + " : ", e);
        }
        logger.log(Level.DEBUG, "Row was updated: {}", isBlocked);
        return isBlocked;
    }

//    @Override
//    public boolean deleteById(long id) throws DaoException {
//        boolean isDeleted;
//        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
//             PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID_QUERY)) {
//            statement.setLong(1, id);
//            isDeleted = statement.executeUpdate() == ONE_CONSTANT;
//        } catch (SQLException e) {
//            throw new DaoException("Failed to delete user by id " + id + " : ", e);
//        }
//        logger.log(Level.DEBUG, "Row was updated: {}", isDeleted);
//        return isDeleted;
//    }

    @Override
    public boolean restoreById(long id) throws DaoException {
        boolean isRestored;
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(RESTORE_BY_ID_QUERY)) {
            statement.setLong(1, id);
            isRestored = statement.executeUpdate() == ONE_CONSTANT;
        } catch (SQLException e) {
            throw new DaoException("Failed to restore user by id " + id + " : ", e);
        }
        logger.log(Level.DEBUG, "Row was updated: {}", isRestored);
        return isRestored;
    }

    @Override
    public boolean updatePasswordById(long id, String password) throws DaoException {
        boolean isUpdated;
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PASSWORD_BY_ID_QUERY)) {
            statement.setString(1, password);
            statement.setLong(2, id);
            isUpdated = statement.executeUpdate() == ONE_CONSTANT;
        } catch (SQLException e) {
            throw new DaoException("Failed to update user's password: ", e);
        }
        logger.log(Level.DEBUG, "Row was updated: {}", isUpdated);
        return isUpdated;
    }

    @Override
    public String getPasswordByLogin(String login) throws DaoException {
        String password = null;
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PASSWORD_BY_LOGIN_QUERY)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    password = resultSet.getString(1);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to get password by login: ", e);
        }
        logger.log(Level.DEBUG, "Found password by login {}: {}", login, password);
        return password;
    }

    @Override
    public String getPasswordByEmail(String email) throws DaoException {
        String password = null;
        try (Connection connection = CustomConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PASSWORD_BY_EMAIL_QUERY)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    password = resultSet.getString(1);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to get password by email: ", e);
        }
        logger.log(Level.DEBUG, "Found password by email {}: {}", email, password);
        return password;
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
