package com.example.spellingcheck.model.dto.request;

import com.example.spellingcheck.annotation.Password;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    @NotBlank
    private String username;
    @NotBlank
    @Password(message = "weak password")
    private String password;
    @NotBlank
    private String fullname;
}
