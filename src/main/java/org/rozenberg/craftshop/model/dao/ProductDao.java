package org.rozenberg.craftshop.model.dao;

import org.rozenberg.craftshop.exception.DaoException;
import org.rozenberg.craftshop.model.entity.Product;

import java.util.List;

public interface ProductDao {
    Product create(Product product) throws DaoException;
    Product updateById(Product product) throws DaoException;
    int deleteById(long id) throws DaoException;
    Product getById(long id) throws DaoException;
    List<Product> getAll() throws DaoException;
    int restoreById(long id) throws DaoException;
}
