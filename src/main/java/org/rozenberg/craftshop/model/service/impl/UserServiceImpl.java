package org.rozenberg.craftshop.model.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rozenberg.craftshop.exception.ServiceException;
import org.rozenberg.craftshop.model.entity.User;
import org.rozenberg.craftshop.model.service.UserService;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger();


    @Override
    public User create(User user) throws ServiceException {
        return null;
    }

    @Override
    public boolean sighIn(String login, String password) throws ServiceException {
        return false;
    }

    @Override
    public List<User> findAll() throws ServiceException {
        return null;
    }

    @Override
    public Optional<User> findById(Long id) throws ServiceException {
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
