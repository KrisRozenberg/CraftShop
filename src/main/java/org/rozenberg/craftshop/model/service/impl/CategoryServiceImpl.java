package org.rozenberg.craftshop.model.service.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rozenberg.craftshop.exception.DaoException;
import org.rozenberg.craftshop.exception.ServiceException;
import org.rozenberg.craftshop.model.dao.CategoryDao;
import org.rozenberg.craftshop.model.dao.impl.CategoryDaoImpl;
import org.rozenberg.craftshop.model.entity.Category;
import org.rozenberg.craftshop.model.service.CategoryService;

public class CategoryServiceImpl implements CategoryService {
    private static final Logger logger = LogManager.getLogger();
    private static CategoryServiceImpl instance;
    private final CategoryDao categoryDao = CategoryDaoImpl.getInstance();

    private CategoryServiceImpl() {
    }

    public static CategoryServiceImpl getInstance() {
        if (instance == null) {
            instance = new CategoryServiceImpl();
        }
        return instance;
    }

    @Override
    public Category create(Category category) throws ServiceException {
        Category newCategory;
        try {
            newCategory = categoryDao.create(category);
        } catch (DaoException e) {
            throw new ServiceException("create() - Failed to create category ", e);
        }
        logger.log(Level.DEBUG, "Category was created: {}", newCategory);
        return newCategory;
    }

    @Override
    public Category getById(long id) throws ServiceException {
        Category category;
        try {
            category = categoryDao.getById(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to find category ", e);
        }
        logger.log(Level.DEBUG, "Category was found: {}", category);
        return category;
    }
}
