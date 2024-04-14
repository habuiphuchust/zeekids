package com.example.spellingcheck.service.implement;

import com.example.spellingcheck.authentication.custom.CustomUserDetails;
import com.example.spellingcheck.authentication.jwt.JwtTokenProvider;
import com.example.spellingcheck.exception.CustomException;
import com.example.spellingcheck.exception.ExceptionCode;
import com.example.spellingcheck.model.dto.request.LoginDTO;
import com.example.spellingcheck.model.dto.request.RegisterDTO;
import com.example.spellingcheck.model.dto.response.AuthenticationDTO;
import com.example.spellingcheck.model.entity.Role;
import com.example.spellingcheck.model.entity.User;
import com.example.spellingcheck.repository.RoleRepository;
import com.example.spellingcheck.repository.UserRepository;
import com.example.spellingcheck.service.IAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public ResponseEntity<String> register(RegisterDTO request) {
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

        String msg = "Register sucessfully";
        return ResponseEntity.ok(msg);
    }

    @Override
    public ResponseEntity<AuthenticationDTO> login(LoginDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        System.out.println(authentication.getName());
        List<String> roles = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            String authority = grantedAuthority.getAuthority();
            roles.add(authority);
        }
        AuthenticationDTO authenticationDTO = AuthenticationDTO.builder()
                .roles(roles)
                .token(jwtTokenProvider.generateToken(authentication))
                .username(authentication.getName())
                .fullname(((CustomUserDetails)authentication.getPrincipal()).getUser().getFullName())
                .build();
        return ResponseEntity.ok(authenticationDTO);
    }

    @Override
    public User getUser() throws RuntimeException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        throw new CustomException(ExceptionCode.UNAUTHORIZED);
    }
}
