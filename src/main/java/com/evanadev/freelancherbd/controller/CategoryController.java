package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.model.Category;
import com.evanadev.freelancherbd.repository.CategoryRepository;
import com.evanadev.freelancherbd.service.CategoryService;
import com.evanadev.freelancherbd.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Controller
public class CategoryController {

    @Autowired
    private final CategoryService categoryService;
    private CategoryRepository categoryRepository;
    @Autowired private AESUtil aesUtil;

    public CategoryController(CategoryService categoryService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/admin/category/create")
    public String create_category(Model model){
        model.addAttribute("category", new Category());
        return "category_form";
    }

    @GetMapping("/admin/category/update_form")
    public String categoryUpdate(@RequestParam("encId") String encId, Model model) {
        System.out.println("category_id="+ encId);
        if (encId != null) {
            Long did = aesUtil.decryptId(encId);
            Category category = categoryRepository.findById(did)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            model.addAttribute("category", category);
        } else {
            model.addAttribute("category", new Category()); // empty object for create
        }
        return "category_form";

    }

    //Save new Category
    @PostMapping("/admin/category/submit_category")
    public String category_submit(@ModelAttribute Category category, Model model){

        String message = "";
        List<Category> singlecategory = null;
            if(category.getId() == null){
                if(categoryRepository.existsCategoriesByCategoryName(category.getCategoryName())){
                    message = "Category Already Exists!";
                }else {
                    category = categoryService.create_category(category);
                    message = "Category Created Successfully!";
                }
            }else{
              categoryService.update_category(category); // service will handle update if id exists
              message = "Category Updated Successfully!";
            }
           singlecategory = Collections.singletonList(category);

        model.addAttribute("messsage", message);
        model.addAttribute("category", new Category());
        model.addAttribute("singleCategory", singlecategory);
        model.addAttribute("aesUtil", aesUtil);
        return "category_form";
    }

    // category Deletion
    @GetMapping("/admin/category/delete_category")
    public String categoryDelete(@RequestParam("encId") String encId, Model model) {
        if (encId != null) {
            Long id = aesUtil.decryptId(encId);
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            categoryRepository.deleteById(category.getId());
           model.addAttribute("category", new Category());
           model.addAttribute("messsage", "Category Deleted Successfully!");
        }else {
            model.addAttribute("category", new Category()); // empty object for create
        }
        return "category_form";
    }

    // Category Listing
    @GetMapping("/admin/category/category_list")
    public String categoryUpdate(Model model) {

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        return "category_list";
    }

}
