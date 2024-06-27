package com.example.spellingcheck.service;

import com.example.spellingcheck.model.dto.request.RegisterDTO;
import com.example.spellingcheck.model.dto.response.UserDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IUserService {
    ResponseEntity<List<UserDTO>> findAllUsers();
    ResponseEntity<String> deleteUser(String name);
    ResponseEntity<String> addUser(RegisterDTO request);

}
