package com.project.shopapp.controllers;

import com.project.shopapp.dtos.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {
    @GetMapping("/order/{order_id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("order_id") Long orderId)
    {
        return ResponseEntity.ok("Get all order successfully");
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("user_id") Long userId)
    {
        return ResponseEntity.ok("Get all order successfully");
    }

    @PutMapping("/{order_id}")
    public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable("order_id") Long orderId, @RequestBody OrderDetailDTO orderDetailDTO)
    {
        return ResponseEntity.ok("Get all order successfully");
    }

    @DeleteMapping("/{order_id}")
    public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable("order_id") Long orderId)
    {
        return ResponseEntity.ok("Delete mapping order detail");
    }
}
