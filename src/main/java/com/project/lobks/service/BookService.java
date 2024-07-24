package com.project.lobks.service;

import com.project.lobks.dto.BookDTO;
import com.project.lobks.dto.BookUpdateDTO;
import com.project.lobks.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> readAllBooks();
    Book readBookById(Long id);
    Book createBook(BookDTO bookDTO);
    Book updateBook(BookUpdateDTO bookUpdateDTO);
    void deleteBook(Long id);
    List<Book> readAllBooksByAuthorId(Long id);
}
