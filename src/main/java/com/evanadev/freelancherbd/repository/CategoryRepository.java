package com.evanadev.freelancherbd.repository;

import com.evanadev.freelancherbd.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long>   {
    boolean existsCategoriesByCategoryName(String categoryName);
    Category findByCategoryName(String categoryName);
    Optional<Category> findById(Long id);
}
