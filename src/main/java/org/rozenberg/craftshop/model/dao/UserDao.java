package org.rozenberg.craftshop.model.dao;

import org.rozenberg.craftshop.exception.DaoException;
import org.rozenberg.craftshop.model.entity.User;

import java.util.List;

public interface UserDao {
    User getById(Long id) throws DaoException;
    User getByLogin(String login) throws DaoException;
    User getByEmail(String email) throws DaoException;
    List<User> getAll() throws DaoException;
    User createAdmin(User user) throws DaoException;
    User updateById(User user) throws DaoException;
    int blockById(long id) throws DaoException;
    int deleteById(long id) throws DaoException;
    int restoreById(long id) throws DaoException;
    int updatePassword(long id, String password) throws DaoException;
}
