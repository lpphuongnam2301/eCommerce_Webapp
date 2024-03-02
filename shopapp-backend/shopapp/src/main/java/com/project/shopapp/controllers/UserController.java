package com.project.shopapp.controllers;

import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.dtos.UserLoginDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO, BindingResult result)
    {
        try {
            if (result.hasErrors())
            {
                List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();

                return ResponseEntity.badRequest().body(errorMessages);
            }
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword()))
            {
                return ResponseEntity.badRequest().body("Password does not match");
            } else {

            }
            return ResponseEntity.ok("Register successfully");
        } catch (Exception err)
        {
            return ResponseEntity.badRequest().body(err.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@Valid @RequestBody UserLoginDTO userLoginDTO, BindingResult result)
    {
        try {
            if (result.hasErrors())
            {
                List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();

                return ResponseEntity.badRequest().body(errorMessages);
            }

            return ResponseEntity.ok("Login successfully!");
        } catch (Exception err)
        {
            return ResponseEntity.badRequest().body(err.getMessage());
        }
    }
}
