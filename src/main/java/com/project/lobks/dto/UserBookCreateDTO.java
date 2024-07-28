package com.project.lobks.dto;

import com.project.lobks.entity.Book;
import com.project.lobks.entity.enums.StatusBook;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserBookCreateDTO {

    private Book book;
    private StatusBook statusBook;
}
