package org.rozenberg.craftshop.model.dao;

import org.rozenberg.craftshop.exception.DaoException;
import org.rozenberg.craftshop.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> getById(Long id) throws DaoException;
    Optional<User> getByLogin(String login) throws DaoException;
    Optional<User> getByLoginAndPassword(String login, String password) throws DaoException;
    Optional<User> getByEmail(String email) throws DaoException;
    List<User> getAll() throws DaoException;
    User create(User user) throws DaoException;
    User updateById(User user) throws DaoException;
    boolean blockById(long id) throws DaoException;
    boolean deleteById(long id) throws DaoException;
    boolean restoreById(long id) throws DaoException;
    boolean updatePasswordById(long id, String password) throws DaoException;
    String getPasswordByLogin(String login) throws DaoException;
    String getPasswordByEmail(String email) throws DaoException;
}
