package org.rozenberg.craftshop.model.dao;

import org.rozenberg.craftshop.exception.DaoException;
import org.rozenberg.craftshop.model.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDao {
    Product create(Product product) throws DaoException;
    Product updateById(Product product) throws DaoException;
    boolean deleteById(long id) throws DaoException;
    Optional<Product> getById(long id) throws DaoException;
    List<Product> getAll() throws DaoException;
    boolean restoreById(long id) throws DaoException;
    List<Product> getByCategoryName(String name) throws DaoException;
    List<Product> getAllInStock(String name) throws DaoException;
}
