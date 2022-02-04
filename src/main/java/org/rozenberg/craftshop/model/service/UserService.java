package org.rozenberg.craftshop.model.service;

import org.rozenberg.craftshop.exception.ServiceException;
import org.rozenberg.craftshop.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User create(User user) throws ServiceException;
    boolean sighIn(String login, String password) throws ServiceException;
    List<User> findAll() throws ServiceException;
    Optional<User> findById(Long id) throws ServiceException;
    User updateById(User user) throws ServiceException;
    boolean deleteById(Long id) throws ServiceException;
    boolean restoreById(Long id) throws ServiceException;
    boolean isLoginExist(String login) throws ServiceException;
    boolean isEmailExist(String email) throws ServiceException;
    boolean checkUserPassword(Long id, String password) throws ServiceException;
    boolean updatePassword(Long id, String password) throws ServiceException;
}
