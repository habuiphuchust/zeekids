package com.example.spellingcheck.service.implement;

import com.example.spellingcheck.model.dto.response.UserDTO;
import com.example.spellingcheck.model.entity.Role;
import com.example.spellingcheck.model.entity.User;
import com.example.spellingcheck.repository.UserRepository;
import com.example.spellingcheck.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    @Override
    public ResponseEntity<List<UserDTO>> findAllUsers() {
        List<User> userList = userRepository.findAll();
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user: userList) {
            List<String> roles = new ArrayList<>();
            for (Role role: user.getRoles()) {
                roles.add(role.getName());
            }
            UserDTO userDTO = UserDTO.builder().username(user.getUsername())
                    .fullname(user.getFullName())
                    .roles(roles)
                    .build();
            userDTOS.add(userDTO);
        }

        return ResponseEntity.ok(userDTOS);
    }

    @Override
    public ResponseEntity<String> deleteUser(String name) {
        Optional<User> userOptional = userRepository.findByUsername(name);
        String msg = "not found user";
        if (userOptional.isPresent()) {
            for (Role role : userOptional.get().getRoles()) {
                if (Objects.equals(role.getName(), "ROLE_ADMIN"))
                    return ResponseEntity.badRequest().body("can't delete admin user");
            }
            userRepository.delete(userOptional.get());
            msg = "delete user success";
        }
        return ResponseEntity.ok(msg);

    }
}
