package com.project.shopapp.controllers;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<?> getAllCategories
            ( @RequestParam("pages") int pages,
              @RequestParam("limit") int limit )
    {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping("")
    public ResponseEntity<?> createCategory
            ( @Valid @RequestBody CategoryDTO categoryDTO
              ,BindingResult result)
    {
            if (result.hasErrors())
            {
                List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();

                return ResponseEntity.badRequest().body(errorMessages);
            }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("Insert categories successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategories(@PathVariable long id, @RequestBody CategoryDTO categoryDTO)
    {
        categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok("Update categories (id: " + id + ") successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategories(@PathVariable long id)
    {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Delete categories (id: " + id + ") successfully");
    }
}
