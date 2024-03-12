package com.project.shopapp.controllers;

import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.dtos.UserLoginDTO;
import com.project.shopapp.models.User;
import com.project.shopapp.responses.LoginResponse;
import com.project.shopapp.services.UserService;
import com.project.shopapp.components.LocalizationUtil;
import com.project.shopapp.utils.MessageLanguageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final UserService userService;
    private final LocalizationUtil localizationUtil;
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
            }

            User user = userService.createUser(userDTO);

            return ResponseEntity.ok(user);
        } catch (Exception err)
        {
            return ResponseEntity.badRequest().body(err.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@Valid @RequestBody UserLoginDTO userLoginDTO)
    {
        try {
            String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
            //Locale locale = localeResolver.resolveLocale(request);
            return ResponseEntity.ok(LoginResponse.builder()
                    .message(localizationUtil.getLocale(MessageLanguageKeys.LOGIN_SUCCESS))
                            .token(token).build());
        } catch (Exception err)
        {
            return ResponseEntity.badRequest().body(LoginResponse.builder()
                    .message(localizationUtil.getLocale(MessageLanguageKeys.LOGIN_FAILED,err.getMessage())).build());
        }
    }
}
