package com.project.lobks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserBookDTO {
    private Long user_id;
    private Long book_id;
}
