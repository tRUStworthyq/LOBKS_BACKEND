package com.project.lobks.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Permission {
    USER_READ("user:read"),
    USER_WRITE("user:write");

    private final String permission;

}
