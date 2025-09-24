package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.model.Category;
import com.evanadev.freelancherbd.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        Optional<Category> existingCategory = categoryRepository.findById(category.getId());
        if(existingCategory.isPresent()) {
            category =  existingCategory.get();
            category.setCategoryName(category.getCategoryName());
            category.setDescription(category.getDescription());
            categoryRepository.save(category);
        }
        //Category savedCategory = categoryRepository.save(category);
        return ;
    }
}
