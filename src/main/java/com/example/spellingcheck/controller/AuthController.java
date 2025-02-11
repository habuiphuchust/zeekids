package com.example.spellingcheck.controller;

import com.example.spellingcheck.exception.ValidationException;
import com.example.spellingcheck.model.dto.request.ChangeUserDTO;
import com.example.spellingcheck.model.dto.request.LoginDTO;
import com.example.spellingcheck.model.dto.request.RegisterDTO;
import com.example.spellingcheck.model.dto.response.AuthenticationDTO;
import com.example.spellingcheck.service.IAuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.time.Instant;


@RestController
@AllArgsConstructor
public class AuthController {
    private IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationDTO> login(@Valid @RequestBody LoginDTO request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new ValidationException(bindingResult);
        return authService.login(request);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDTO request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new ValidationException(bindingResult);
        return authService.register(request);
    }

    @GetMapping("/myinfo")
    public ResponseEntity<AuthenticationDTO> myinfo() {
        return authService.getUser();
    }

    @GetMapping("/epoch-time")
    public long getEpochTime() {
        return Instant.now().toEpochMilli();
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangeUserDTO request) {
        return authService.changePassword(request);
    }
}
