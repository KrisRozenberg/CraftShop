package org.rozenberg.craftshop.model.service.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rozenberg.craftshop.exception.DaoException;
import org.rozenberg.craftshop.exception.ServiceException;
import org.rozenberg.craftshop.model.dao.CategoryDao;
import org.rozenberg.craftshop.model.dao.UserDao;
import org.rozenberg.craftshop.model.dao.impl.CategoryDaoImpl;
import org.rozenberg.craftshop.model.dao.impl.UserDaoImpl;
import org.rozenberg.craftshop.model.entity.Category;
import org.rozenberg.craftshop.model.entity.User;
import org.rozenberg.craftshop.model.service.UserService;
import org.rozenberg.craftshop.util.PasswordEncoder;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger();

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
    public boolean isLoginExist(String login) throws ServiceException {
        return false;
    }

    @Override
    public boolean isEmailExist(String email) throws ServiceException {
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
