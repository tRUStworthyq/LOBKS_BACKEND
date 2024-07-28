package com.project.lobks.service;

import com.project.lobks.dto.UserBookCreateDTO;
import com.project.lobks.dto.UserBookDTO;
import com.project.lobks.dto.UserDTO;
import com.project.lobks.entity.Book;
import com.project.lobks.entity.UserBookEmbeddable;

import java.util.List;

public interface UserBookService {
    List<Book> findBooksByUserId(Long id);
    List<UserDTO> findUsersByBookId(Long id);
    UserBookCreateDTO saveUserBook(UserBookDTO userBookDTO, String jwt);
    void deleteUserBook(UserBookEmbeddable userBookEmbeddable, String jwt);
}
