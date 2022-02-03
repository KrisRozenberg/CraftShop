package org.rozenberg.craftshop.model.service;

import org.rozenberg.craftshop.exception.ServiceException;
import org.rozenberg.craftshop.model.entity.Category;

public interface CategoryService {
    Category create(Category category) throws ServiceException;
    Category getById(long id) throws ServiceException;
}
