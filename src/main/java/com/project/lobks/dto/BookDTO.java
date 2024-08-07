package com.project.lobks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BookDTO {
    private String name;
    private String description;
    private Long authorId;
}
