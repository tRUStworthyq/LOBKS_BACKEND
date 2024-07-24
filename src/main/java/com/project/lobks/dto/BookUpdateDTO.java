package com.project.lobks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookUpdateDTO {

    private Long id;
    private String name;
    private String status;
    private Long authorId;
}
