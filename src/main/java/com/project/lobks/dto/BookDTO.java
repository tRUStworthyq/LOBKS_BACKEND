package com.project.lobks.dto;

import com.project.lobks.entity.Author;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BookDTO {
    private String name;
    private String status;
    private Long authorId;
}
