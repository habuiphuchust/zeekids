package com.example.spellingcheck.service;

import com.example.spellingcheck.model.dto.request.LoginDTO;
import com.example.spellingcheck.model.dto.request.RegisterDTO;
import com.example.spellingcheck.model.dto.response.AuthenticationDTO;
import com.example.spellingcheck.model.entity.User;
import org.springframework.http.ResponseEntity;

public interface IAuthService {
    ResponseEntity<String> register(RegisterDTO request);

    ResponseEntity<AuthenticationDTO> login(LoginDTO request);

    ResponseEntity<AuthenticationDTO> getUser() throws RuntimeException;
}
