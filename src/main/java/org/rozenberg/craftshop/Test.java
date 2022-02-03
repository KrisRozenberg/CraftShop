package org.rozenberg.craftshop;

import org.rozenberg.craftshop.exception.ServiceException;
import org.rozenberg.craftshop.model.entity.Category;
import org.rozenberg.craftshop.model.service.CategoryService;
import org.rozenberg.craftshop.model.service.impl.CategoryServiceImpl;

public class Test {
    public static void main(String[] args) throws ServiceException {
        CategoryService categoryService = CategoryServiceImpl.getInstance();
        categoryService.create(new Category("Нитки для вышивания"));
    }
}
