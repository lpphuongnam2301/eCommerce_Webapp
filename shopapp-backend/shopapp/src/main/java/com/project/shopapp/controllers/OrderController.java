package com.project.shopapp.controllers;

import com.project.shopapp.dtos.OrderDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getOrder(@Valid @PathVariable("user_id") Long userId)
    {
        return ResponseEntity.ok("Get all order successfully");
    }
    @PostMapping("")
    public ResponseEntity<?> createOrder (@Valid @RequestBody OrderDTO orderDTO, BindingResult result)
    {
        try {
            if (result.hasErrors())
            {
                List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();

                return ResponseEntity.badRequest().body(errorMessages);
            }

            return ResponseEntity.ok("Create order successfully!");
        } catch (Exception err)
        {
            return ResponseEntity.badRequest().body(err.getMessage());
        }
    }
    @PutMapping("/{order_id}")
    public ResponseEntity<?> updatetOrder(@Valid @PathVariable("order_id") Long order_id, @RequestBody @Valid OrderDTO orderDTO)
    {
        return ResponseEntity.ok("Update order successfully");
    }

    @DeleteMapping("/{order_id}")
    public ResponseEntity<?> deleteOrder(@Valid @PathVariable("order_id") Long order_id)
    {
        return ResponseEntity.ok("Delete order successfully");
    }

}
