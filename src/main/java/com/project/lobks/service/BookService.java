package com.project.lobks.service;

import com.project.lobks.dto.BookDTO;
import com.project.lobks.entity.Book;
import com.project.lobks.repository.BookRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class BookService {
    @Autowired
    private final BookRepository bookRepository;


    public List<Book> readAllBooks(){
        return bookRepository.findAll();
    }
    public Book readBookById(long id){
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException(""));
    }

    public Book createBook(BookDTO bookDTO){
        return bookRepository.save(Book
                .builder()
                .name(bookDTO.getName())
                .author(bookDTO.getAuthor())
                .status(bookDTO.getStatus())
                .build());
    }

    public Book updateBook(Book book){
        return bookRepository.save(book);
    }
    public void deleteBook(long id){
        bookRepository.deleteById(id);
    }
}
