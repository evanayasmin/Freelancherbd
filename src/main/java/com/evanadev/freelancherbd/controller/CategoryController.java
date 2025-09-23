package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.model.Category;
import com.evanadev.freelancherbd.repository.CategoryRepository;
import com.evanadev.freelancherbd.repository.UserProfileRepository;
import com.evanadev.freelancherbd.repository.UserRepository;
import com.evanadev.freelancherbd.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class CategoryController {

    @Autowired
    private final CategoryService categoryService;
    private CategoryRepository categoryRepository;

    public CategoryController(CategoryService categoryService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/admin/category/create")
    public String create_category(Model model){
        model.addAttribute("category", new Category());
        return "category_form";
    }

    @PostMapping("/admin/category/submit_category")
    public String category_submit(@ModelAttribute Category category, Model model){

        String message = "";
        if(categoryRepository.existsCategoriesByCategoryName(category.getCategoryName())){
            message = "Category Already Exists!";
        }else{
            message = "Category Created Successfully!";
            category = categoryService.create_category(category);
        }
        model.addAttribute("messsage", message);
        model.addAttribute("category", category);
        return "category_form";
    }
}
