package com.example.spellingcheck.service;

import com.example.spellingcheck.exception.CustomException;
import com.example.spellingcheck.model.dto.request.ChangeUserDTO;
import com.example.spellingcheck.model.dto.request.LoginDTO;
import com.example.spellingcheck.model.dto.request.RegisterDTO;
import com.example.spellingcheck.model.dto.response.AuthenticationDTO;
import org.springframework.http.ResponseEntity;

public interface IAuthService {
    ResponseEntity<String> register(RegisterDTO request);

    ResponseEntity<AuthenticationDTO> login(LoginDTO request);

    ResponseEntity<String> changePassword(ChangeUserDTO request);

    ResponseEntity<AuthenticationDTO> getUser() throws CustomException;
}
