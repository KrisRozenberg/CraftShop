package org.rozenberg.craftshop.model.service;

import org.rozenberg.craftshop.exception.ServiceException;
import org.rozenberg.craftshop.model.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    User createAdmin(User user) throws ServiceException;
    Optional<User> sighIn(String login, String password) throws ServiceException;
    List<User> findAll() throws ServiceException;
    User findById(Long id) throws ServiceException;
    User updateById(User user) throws ServiceException;
    boolean deleteById(Long id) throws ServiceException;
    boolean restoreById(Long id) throws ServiceException;
    boolean validateInputPasswords(Map<String, String> formData);
    boolean validateUserData(Map<String, String> formData);
    boolean checkIfLoginExist(String login) throws ServiceException;
    boolean checkIfEmailExist(String email) throws ServiceException;
    boolean checkUserPassword(Long id, String password) throws ServiceException;
    boolean updatePassword(Long id, String password) throws ServiceException;
}
