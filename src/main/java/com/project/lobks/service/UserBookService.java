package com.project.lobks.service;

import com.project.lobks.dto.UserBookCreateDTO;
import com.project.lobks.dto.UserBookDTO;
import com.project.lobks.dto.UserDTO;
import com.project.lobks.entity.UserBook;
import com.project.lobks.entity.UserBookEmbeddable;
import com.project.lobks.entity.enums.StatusBook;

import java.util.List;

public interface UserBookService {
    List<UserBookCreateDTO> findBooksByUserId(Long id);
    List<UserDTO> findUsersByBookId(Long id);
    UserBookCreateDTO saveUserBook(UserBookDTO userBookDTO, String jwt);
    void deleteUserBook(UserBookEmbeddable userBookEmbeddable, String jwt);
    StatusBook changeStatusBookEmbeddableId(UserBook userBook, String jwt);
}
