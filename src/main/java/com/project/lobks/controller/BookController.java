package com.project.lobks.controller;

import com.project.lobks.dto.BookDTO;
import com.project.lobks.dto.BookUpdateDTO;
import com.project.lobks.entity.Book;
import com.project.lobks.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private final BookService bookService;

    @PreAuthorize("hasAuthority('user:read')")
    @GetMapping("/book/{id}")
    public ResponseEntity<Book> readBookById(@PathVariable Long id){
        return new ResponseEntity<>(bookService.readBookById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:read')")
    @GetMapping("/")
    public ResponseEntity<List<Book>> readAllBooks(){
        return new ResponseEntity<>(bookService.readAllBooks(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:read')")
    @GetMapping("/all/{id}")
    public ResponseEntity<List<Book>> readAllBooksByAuthorId(@PathVariable Long id) {
        return new ResponseEntity<>(bookService.readAllBooksByAuthorId(id), HttpStatus.OK);

    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping("/create")
    public ResponseEntity<Book> createBook(@RequestBody BookDTO bookDTO){
        return new ResponseEntity<>(bookService.createBook(bookDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PutMapping("/update")
    public ResponseEntity<Book> updateBook(@RequestBody BookUpdateDTO bookUpdateDTO){
        return new ResponseEntity<>(bookService.updateBook(bookUpdateDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @DeleteMapping("/{id}")
    public HttpStatus deleteBook(@PathVariable long id){
        bookService.deleteBook(id);
        return HttpStatus.OK;
    }
}
