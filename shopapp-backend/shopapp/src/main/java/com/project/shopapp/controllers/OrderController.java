package com.project.shopapp.controllers;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.models.Order;
import com.project.shopapp.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getOrderByUserId(@Valid @PathVariable("user_id") Long userId)
    {
        List<Order> orders = orderService.findByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@Valid @PathVariable("id") Long id)
    {
        Order order = orderService.getOrder(id);
        return ResponseEntity.ok(order);
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
            orderService.createOrder(orderDTO);
            return ResponseEntity.ok("Create order successfully!");
        } catch (Exception err)
        {
            return ResponseEntity.badRequest().body(err.getMessage());
        }
    }
    @PutMapping("/{order_id}")
    public ResponseEntity<?> updatetOrder(@Valid @PathVariable("order_id") Long order_id, @RequestBody @Valid OrderDTO orderDTO)
    {
        try {
            Order oder = orderService.updateOrder(order_id, orderDTO);
            return ResponseEntity.ok(oder);
        }
        catch (Exception err)
        {
            return ResponseEntity.badRequest().body(err.getMessage());
        }
    }

    @DeleteMapping("/{order_id}")
    public ResponseEntity<?> deleteOrder(@Valid @PathVariable("order_id") Long order_id)
    {
        orderService.deleteOrder(order_id);
        return ResponseEntity.ok("Delete order successfully");
    }

}
