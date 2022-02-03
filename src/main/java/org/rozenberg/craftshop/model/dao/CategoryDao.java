package org.rozenberg.craftshop.model.dao;

import org.rozenberg.craftshop.exception.DaoException;
import org.rozenberg.craftshop.model.entity.Category;

public interface CategoryDao {
    Category create(Category category) throws DaoException;
    Category getById(long id) throws DaoException;
}
