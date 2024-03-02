package com.project.shopapp.controllers;

import com.project.shopapp.dtos.CategoryDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {

    @GetMapping("")
    public ResponseEntity<String> getAllCategories
            ( @RequestParam("pages") int pages,
              @RequestParam("limit") int limit )
    {
        return ResponseEntity.ok("EM ne anh" + pages + limit);
    }

    @PostMapping("")
    public ResponseEntity<?> insertCategory
            ( @Valid @RequestBody CategoryDTO categoryDTO
              ,BindingResult result)
    {
            if (result.hasErrors())
            {
                List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();

                return ResponseEntity.badRequest().body(errorMessages);
            }

        return ResponseEntity.ok("hello insert nhe" + categoryDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategories(@PathVariable long id)
    {
        return ResponseEntity.ok("Update nhe" + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategories(@PathVariable long id)
    {
        return ResponseEntity.ok("delete ne" + id);
    }
}
