package com.project.lobks.service;

import com.project.lobks.dto.BookDTO;
import com.project.lobks.dto.BookUpdateDTO;
import com.project.lobks.entity.Author;
import com.project.lobks.entity.Book;
import com.project.lobks.repository.AuthorRepository;
import com.project.lobks.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Service
public class BookServiceImpl implements BookService{
    @Autowired
    private final BookRepository bookRepository;

    @Autowired
    private final AuthorRepository authorRepository;


    @Override
    public List<Book> readAllBooks(){
        return bookRepository.findAll();
    }

    @Override
    public Book readBookById(Long id){
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException(""));
    }

    @Override
    public Book createBook(BookDTO bookDTO){
        Optional<Author> optionalAuthor = authorRepository
                .findById(bookDTO.getAuthorId());
        if (optionalAuthor.isPresent()) {
            return bookRepository.save(Book
                    .builder()
                    .name(bookDTO.getName())
                    .author(optionalAuthor.get())
                    .status(bookDTO.getStatus())
                    .build());
        }
        throw new EntityNotFoundException("Author with id=" + bookDTO.getAuthorId() + " didn't found");
    }

    @Override
    public Book updateBook(BookUpdateDTO bookUpdateDTO){
        Optional<Book> optionalBook = bookRepository.findById(bookUpdateDTO.getId());
        if (optionalBook.isPresent()) {
            Optional<Author> optionalAuthor = authorRepository
                    .findById(bookUpdateDTO.getAuthorId());
            if (optionalAuthor.isPresent()) {
                Book newBook = optionalBook.get();
                newBook.setName(bookUpdateDTO.getName());
                newBook.setStatus(bookUpdateDTO.getStatus());
                newBook.setAuthor(optionalAuthor.get());

                return bookRepository.save(newBook);
            }
        }
        throw new EntityNotFoundException("Book or author doesn't exist");
    }

    @Override
    public List<Book> readAllBooksByAuthorId(Long id) {
        return bookRepository.findBooksByAuthorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Books doesn't exist"));
    }

    @Override
    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }
}
