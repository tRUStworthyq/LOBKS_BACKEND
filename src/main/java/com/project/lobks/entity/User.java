package com.project.lobks.entity;

import com.project.lobks.entity.enums.Role;
import com.project.lobks.entity.enums.StatusUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private StatusUser statusUser;
    @Enumerated(EnumType.STRING)
    private Role role;

}
