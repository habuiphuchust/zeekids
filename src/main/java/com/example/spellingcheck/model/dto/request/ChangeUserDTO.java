package com.example.spellingcheck.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserDTO {
    @NotBlank
    private String username;
    private String password;
    private String newPassword;
    private String fullname;
}
