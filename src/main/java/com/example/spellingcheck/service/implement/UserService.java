package com.example.spellingcheck.service.implement;

import com.example.spellingcheck.exception.CustomException;
import com.example.spellingcheck.exception.ExceptionCode;
import com.example.spellingcheck.model.dto.request.RegisterDTO;
import com.example.spellingcheck.model.dto.response.UserDTO;
import com.example.spellingcheck.model.entity.Role;
import com.example.spellingcheck.model.entity.User;
import com.example.spellingcheck.repository.RoleRepository;
import com.example.spellingcheck.repository.UserRepository;
import com.example.spellingcheck.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
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

    @Override
    public ResponseEntity<String> addUser(RegisterDTO request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new CustomException(ExceptionCode.USERNAME_ALREADY_EXIST);
        }
        Role role = roleRepository.findByName("ROLE_USER");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        User newUser = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roleSet)
                .fullName(request.getFullname())
                .build();
        userRepository.save(newUser);

        String msg = "Add user sucessfully";
        return ResponseEntity.ok(msg);    }
}
