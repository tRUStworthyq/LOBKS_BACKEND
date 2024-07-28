package com.project.lobks.dto;

import com.project.lobks.entity.enums.StatusUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchDTO {
    private StatusUser statusUser;
}
