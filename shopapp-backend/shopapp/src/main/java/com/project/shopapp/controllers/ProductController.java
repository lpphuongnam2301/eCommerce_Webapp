package com.project.shopapp.controllers;

import com.project.shopapp.dtos.ProductDTO;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    @GetMapping("")
    public ResponseEntity<?> getAllProducts
            ( @PathParam("page") int page,
              @PathParam("limit") int limit)
    {
        return ResponseEntity.ok("day la get all product");
    }

    @PostMapping("")
    public ResponseEntity<?> insertProduct
            (@Valid @RequestBody ProductDTO productDTO
            , BindingResult result )
    {
        if (result.hasErrors())
        {
            List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();

            return ResponseEntity.badRequest().body(errorMessages);
        }
        return ResponseEntity.ok("day la insert product");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable long id)
    {
        return ResponseEntity.ok("day la update product");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable long id)
    {
        return ResponseEntity.ok("day la delete product");
    }
}
