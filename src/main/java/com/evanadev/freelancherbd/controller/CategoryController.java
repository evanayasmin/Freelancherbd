package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.model.Category;
import com.evanadev.freelancherbd.model.Status;
import com.evanadev.freelancherbd.repository.CategoryRepository;
import com.evanadev.freelancherbd.service.CategoryService;
import com.evanadev.freelancherbd.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;


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

    // Update from category update form
    @GetMapping("/admin/category/update_form")
    public String categoryUpdate(@RequestParam("encId") String encId, Model model) {
        System.out.println("category_id="+ encId);
        if (encId != null) {
            Long did = aesUtil.decryptId(encId);
            Category category = categoryRepository.findById(did)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            model.addAttribute("category", category);
            List<Category> singlecategory = Collections.singletonList(category);
            model.addAttribute("singleCategory", singlecategory);
        } else {
            model.addAttribute("category", new Category()); // empty object for create
        }
        return "category_form";

    }

    @GetMapping("/admin/category/update_category")
    public String categoryUpdateById(@RequestParam("encId") String encId, Model model) {
        if (encId != null) {
            Long did = aesUtil.decryptId(encId);
            Category category = categoryRepository.findById(did)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            model.addAttribute("category", category);
            model.addAttribute("statuses", Status.values());
        } else {
            model.addAttribute("messsge", "Category Not found."); // empty object for create
        }
        return "fragments/category_update_form :: updateForm";

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

    //Update One Category BY AJAX
    @PostMapping("/admin/category/update")
    @ResponseBody
    public Map<String, String> category_update(@ModelAttribute Category category){
        Map<String, String> response = new HashMap<>();
        boolean duplicateExists;
        if (category.getId() != null) {
            // Updating existing category → exclude current ID
            duplicateExists = categoryRepository.existsByCategoryNameAndIdNot(
                    category.getCategoryName(), category.getId());
        } else {
            // Creating new category
            duplicateExists = categoryRepository.existsCategoriesByCategoryName(
                    category.getCategoryName());
        }
        if (duplicateExists) {
            response.put("status", "error");
            response.put("message", "Category Already Exists!");
        }
        else{
            categoryService.update_category(category); // service will handle update if id exists
            response.put("status", "success");
            response.put("message", "Category Updated Successfully!");
        }
        return response;
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

    // category Deletion
    @GetMapping("/admin/category/delete_bycategory")
    @ResponseBody
    public ResponseEntity<String> categoryListDelete(@RequestParam("encId") String encId) {
        if (encId != null) {
            Long id = aesUtil.decryptId(encId);
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            if(category.getId() != null) {
                categoryRepository.deleteById(id);
                return ResponseEntity.ok("Category Deleted Successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid encId");
        }
    }

    // Category Listing
    @GetMapping("/admin/category/category_list")
    public String categoryUpdate(Model model) {

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        return "category_list";
    }

}
