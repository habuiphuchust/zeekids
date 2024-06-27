package com.example.spellingcheck.controller;

import com.example.spellingcheck.exception.ValidationException;
import com.example.spellingcheck.model.dto.request.RegisterDTO;
import com.example.spellingcheck.model.dto.response.UserDTO;
import com.example.spellingcheck.model.entity.User;
import com.example.spellingcheck.service.implement.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/users")
@EnableMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public @ResponseBody ResponseEntity<List<UserDTO>> getAllUsers() {
        // This returns a JSON or XML with the users
        return userService.findAllUsers();
    }

    @GetMapping("/delete/{name}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public @ResponseBody ResponseEntity<String> deleteUser(@PathVariable String name) {
        // This returns a JSON or XML with the users
        return userService.deleteUser(name);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<String> addUser(@Valid @RequestBody RegisterDTO request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new ValidationException(bindingResult);
        return userService.addUser(request);
    }
}
