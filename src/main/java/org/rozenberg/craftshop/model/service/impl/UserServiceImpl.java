package org.rozenberg.craftshop.model.service.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rozenberg.craftshop.controller.command.RequestParameter;
import org.rozenberg.craftshop.exception.DaoException;
import org.rozenberg.craftshop.exception.ServiceException;
import org.rozenberg.craftshop.model.dao.CategoryDao;
import org.rozenberg.craftshop.model.dao.UserDao;
import org.rozenberg.craftshop.model.dao.impl.CategoryDaoImpl;
import org.rozenberg.craftshop.model.dao.impl.UserDaoImpl;
import org.rozenberg.craftshop.model.entity.Category;
import org.rozenberg.craftshop.model.entity.User;
import org.rozenberg.craftshop.model.service.UserService;
import org.rozenberg.craftshop.util.DataValidator;
import org.rozenberg.craftshop.util.PasswordEncoder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger();
    private static final String EMPTY_STRING = "";

    private static UserServiceImpl instance;
    private final UserDao userDao = UserDaoImpl.getInstance();

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        if (instance == null) {
            instance = new UserServiceImpl();
        }
        return instance;
    }

    @Override
    public User create(User user) throws ServiceException {
        User newUser;
        String encodedPassword = PasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        try {
            newUser = userDao.create(user);
        } catch (DaoException e) {
            throw new ServiceException("Failed to create user: ", e);
        }
        logger.log(Level.DEBUG, "Created user: {}", newUser);
        return newUser;
    }

    @Override
    public Optional<User> signIn(String login, String password) throws ServiceException {
        Optional<User> user;
        try {
            String encoded = PasswordEncoder.encode(password);
            user = userDao.getByLoginAndPassword(login, encoded);
        } catch (DaoException e) {
            throw new ServiceException("Failed to find user by login and password: ", e);
        }
        logger.log(Level.DEBUG, "User was found: {}", user);
        return user;
    }

    @Override
    public boolean isRegistrationDataValid(Map<String, String> data){
        DataValidator validator = DataValidator.getInstance();
        boolean isValid = true;

        String name = data.get(RequestParameter.NAME);
        if (!validator.isNameValid(name)) {
            data.put(RequestParameter.NAME, EMPTY_STRING);
            isValid = false;
        }

        String surname = data.get(RequestParameter.SURNAME);
        if (!validator.isSurnameValid(surname)) {
            data.put(RequestParameter.SURNAME, EMPTY_STRING);
            isValid = false;
        }

        String login = data.get(RequestParameter.LOGIN);
        if (!validator.isLoginValid(login)) {
            data.put(RequestParameter.LOGIN, EMPTY_STRING);
            isValid = false;
        }

        String password = data.get(RequestParameter.PASSWORD);
//        logger.log(Level.DEBUG, password);
        if (!validator.isPasswordValid(password)) {
            data.put(RequestParameter.PASSWORD, EMPTY_STRING);
            isValid = false;
        }

        String repeatedPassword = data.get(RequestParameter.REPEATED_PASSWORD);
//        logger.log(Level.DEBUG, repeatedPassword);
        if (!validator.isPasswordValid(repeatedPassword)) {
            data.put(RequestParameter.REPEATED_PASSWORD, EMPTY_STRING);
            isValid = false;
        }

        String email = data.get(RequestParameter.EMAIL);
//        logger.log(Level.DEBUG, email);
        if (!validator.isEmailValid(email)) {
            data.put(RequestParameter.EMAIL, EMPTY_STRING);
            isValid = false;
        }

        logger.log(Level.DEBUG, "Registration data is valid: {}", isValid);
        return isValid;
    }

    @Override
    public boolean isLoginExist(String login) throws ServiceException {
        Optional<User> user;
        try {
            user = userDao.getByLogin(login);
        } catch (DaoException e) {
            throw new ServiceException("Failed to check if login exist: ", e);
        }
        logger.log(Level.DEBUG,"User with login {} exist: {}", login, user.isPresent());
        return user.isPresent();
    }

    @Override
    public boolean isEmailExist(String email) throws ServiceException {
        Optional<User> user;
        try {
            user = userDao.getByEmail(email);
        } catch (DaoException e) {
            throw new ServiceException("Failed to check if email exist: ", e);
        }
        logger.log(Level.DEBUG,"User with email {} exist: {}", email, user.isPresent());
        return user.isPresent();
    }

    @Override
    public List<User> getAll() throws ServiceException {
        return null;
    }

    @Override
    public Optional<User> getById(Long id) throws ServiceException {
        return Optional.empty();
    }

    @Override
    public User updateById(User user) throws ServiceException {
        return null;
    }

    @Override
    public boolean deleteById(Long id) throws ServiceException {
        return false;
    }

    @Override
    public boolean restoreById(Long id) throws ServiceException {
        return false;
    }

    @Override
    public boolean checkUserPassword(Long id, String password) throws ServiceException {
        return false;
    }

    @Override
    public boolean updatePassword(Long id, String password) throws ServiceException {
        return false;
    }
}
