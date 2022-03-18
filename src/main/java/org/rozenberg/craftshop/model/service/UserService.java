package org.rozenberg.craftshop.model.service;

import org.rozenberg.craftshop.exception.ServiceException;
import org.rozenberg.craftshop.model.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    User create(User user) throws ServiceException;
    Optional<User> signIn(String login, String password) throws ServiceException;
    boolean isRegistrationDataValid(Map<String, String> data);
    boolean isLoginExist(String login) throws ServiceException;
    boolean isEmailExist(String email) throws ServiceException;

    List<User> getAll() throws ServiceException;
    Optional<User> getById(Long id) throws ServiceException;
    User updateById(User user) throws ServiceException;
    boolean deleteById(Long id) throws ServiceException;
    boolean restoreById(Long id) throws ServiceException;
    boolean checkUserPassword(Long id, String password) throws ServiceException;
    boolean updatePassword(Long id, String password) throws ServiceException;
}
