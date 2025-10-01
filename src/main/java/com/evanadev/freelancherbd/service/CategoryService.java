package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.model.Category;
import com.evanadev.freelancherbd.model.Status;
import com.evanadev.freelancherbd.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public void update_category(Category category) {

        Category existing = categoryRepository.findById(category.getId()).get();
        existing.setCategoryName(category.getCategoryName());
        existing.setDescription(category.getDescription());
        existing.setStatus(category.getStatus());
        categoryRepository.save(category);

    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
