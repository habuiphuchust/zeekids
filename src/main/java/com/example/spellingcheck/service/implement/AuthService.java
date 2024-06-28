package com.example.spellingcheck.service.implement;

import com.example.spellingcheck.authentication.custom.CustomUserDetails;
import com.example.spellingcheck.authentication.jwt.JwtTokenProvider;
import com.example.spellingcheck.exception.CustomException;
import com.example.spellingcheck.exception.ExceptionCode;
import com.example.spellingcheck.model.dto.request.ChangeUserDTO;
import com.example.spellingcheck.model.dto.request.LoginDTO;
import com.example.spellingcheck.model.dto.request.RegisterDTO;
import com.example.spellingcheck.model.dto.response.AuthenticationDTO;
import com.example.spellingcheck.model.entity.Role;
import com.example.spellingcheck.model.entity.User;
import com.example.spellingcheck.repository.RoleRepository;
import com.example.spellingcheck.repository.UserRepository;
import com.example.spellingcheck.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public ResponseEntity<String> changePassword(ChangeUserDTO request) {
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
        if (userOptional.isEmpty()) return ResponseEntity.ok("not found user");
        if (request.getNewPassword().isEmpty()) return ResponseEntity.ok("new password must not empty");

        User user = userOptional.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return ResponseEntity.ok("you not login");
        List<String> roles = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            String authority = grantedAuthority.getAuthority();
            roles.add(authority);
        }
        if (roles.contains("ROLE_ADMIN")) {

            for (Role role : user.getRoles()) {
                if (Objects.equals(role.getName(), "ROLE_ADMIN"))
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(user.getUsername(), request.getPassword()));
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            return ResponseEntity.ok("change password success");
        } else {
            if (!Objects.equals(authentication.getName(), request.getUsername()))
                return ResponseEntity.ok("you aren't the account owner");
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            return ResponseEntity.ok("change password success");
        }

    }

    @Override
    public ResponseEntity<AuthenticationDTO> getUser() throws RuntimeException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            List<String> roles = new ArrayList<>();
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                String authority = grantedAuthority.getAuthority();
                roles.add(authority);
            }
            AuthenticationDTO authenticationDTO = AuthenticationDTO.builder()
                    .roles(roles)
                    .token("")
                    .username(authentication.getName())
                    .fullname(((CustomUserDetails)authentication.getPrincipal()).getUser().getFullName())
                    .build();
            return ResponseEntity.ok(authenticationDTO);
        }
        throw new CustomException(ExceptionCode.UNAUTHORIZED);
    }
}
