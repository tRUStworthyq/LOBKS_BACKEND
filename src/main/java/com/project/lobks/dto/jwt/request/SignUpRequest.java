package com.project.lobks.dto.jwt.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpRequest {
    private String username;
    private String email;
    private String password;
    private String role;
}
