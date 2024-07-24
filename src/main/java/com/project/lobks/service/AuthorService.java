package com.project.lobks.service;

import com.project.lobks.dto.AuthorDTO;
import com.project.lobks.entity.Author;

import java.util.List;

public interface AuthorService {
    List<Author> readAllAuthors();
    Author readAuthorById(Long id);
    Author createAuthor(AuthorDTO authorDTO);
    Author updateAuthor(Author author);
    void deleteAuthor(Long id);
    List<Author> findAuthorsByFirstnameLikeAndLastnameLike(AuthorDTO authorDTO);

}
