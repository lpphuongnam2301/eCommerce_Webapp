package com.project.shopapp.controllers;

import com.project.shopapp.dtos.UserDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO)
    {
        try {
            return ResponseEntity.ok("Register successfully!");
        } catch (Exception err)
        {
            return ResponseEntity.badRequest().body(err.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@Valid @RequestBody UserDTO userDTO)
    {
        try {
            return ResponseEntity.ok("Login successfully!");
        } catch (Exception err)
        {
            return ResponseEntity.badRequest().body(err.getMessage());
        }
    }
}
