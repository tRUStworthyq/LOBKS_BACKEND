package com.project.lobks.dto;

import com.project.lobks.entity.enums.Role;
import com.project.lobks.entity.enums.StatusUser;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {

    private String username;
    private String email;
    private StatusUser statusUser;
    private Role role;
}
