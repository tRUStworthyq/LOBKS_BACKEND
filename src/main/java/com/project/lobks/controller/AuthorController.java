package com.project.lobks.controller;

import com.project.lobks.dto.AuthorDTO;
import com.project.lobks.entity.Author;
import com.project.lobks.service.AuthorService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/authors")
public class AuthorController {

    @Autowired
    private final AuthorService authorService;

    @PreAuthorize("hasAuthority('user:read')")
    @GetMapping("/")
    public ResponseEntity<List<Author>> readAllAuthors() {
        return new ResponseEntity<>(authorService.readAllAuthors(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:read')")
    @GetMapping("/author/{id}")
    public ResponseEntity<Author> readAuthorById(@PathVariable Long id) {
        return new ResponseEntity<>(authorService.readAuthorById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:read')")
    @PostMapping("/search")
    public ResponseEntity<List<Author>> findAuthorsByFirstnameLikeAndLastnameLike(@RequestBody AuthorDTO authorDTO) {
        return new ResponseEntity<>(authorService
                .findAuthorsByFirstnameLikeAndLastnameLike(authorDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping("/create")
    public ResponseEntity<Author> createAuthor(@RequestBody AuthorDTO authorDTO) {
        return new ResponseEntity<>(authorService.createAuthor(authorDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PutMapping("/update")
    public ResponseEntity<Author> updateAuthor(@RequestBody Author author) {
        return new ResponseEntity<>(authorService.updateAuthor(author), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @DeleteMapping("/{id}")
    public HttpStatus deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return HttpStatus.OK;
    }

}
