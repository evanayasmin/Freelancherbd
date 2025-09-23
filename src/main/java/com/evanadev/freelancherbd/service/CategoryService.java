package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.model.Category;
import com.evanadev.freelancherbd.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryService (CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category create_category(Category category) {
        Category savedCategory = categoryRepository.save(category);
        return savedCategory;
    }

}
