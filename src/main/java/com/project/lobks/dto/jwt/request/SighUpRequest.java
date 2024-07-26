package com.project.lobks.dto.jwt.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class SighUpRequest {
    private String username;
    private String email;
    private String password;
    private String role;
}
