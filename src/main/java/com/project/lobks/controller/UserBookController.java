package com.project.lobks.controller;

import com.project.lobks.dto.UserBookCreateDTO;
import com.project.lobks.dto.UserBookDTO;
import com.project.lobks.dto.UserDTO;
import com.project.lobks.entity.Book;
import com.project.lobks.entity.UserBook;
import com.project.lobks.entity.UserBookEmbeddable;
import com.project.lobks.entity.enums.StatusBook;
import com.project.lobks.service.UserBookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/user_books")
public class UserBookController {

    @Autowired
    private UserBookServiceImpl userBookService;

    @PreAuthorize("hasAuthority('user:read')")
    @GetMapping("/books/{id}")
    public ResponseEntity<List<UserBookCreateDTO>> findBooksByUserId(@PathVariable Long id) {
        return new ResponseEntity<>(userBookService.findBooksByUserId(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/users/{id}")
    public ResponseEntity<List<UserDTO>> findUsersByBookId(@PathVariable Long id) {
        return new ResponseEntity<>(userBookService.findUsersByBookId(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:read')")
    @PostMapping("/save")
    public ResponseEntity<UserBookCreateDTO> saveUserBook(@RequestBody UserBookDTO userBookDTO, @RequestHeader("Authorization") String jwt) {
        return new ResponseEntity<>(userBookService.saveUserBook(userBookDTO, jwt), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:read')")
    @PatchMapping("/status")
    public ResponseEntity<StatusBook> changeStatusBookEmbeddableId(@RequestBody UserBook userBook, @RequestHeader("Authorization") String jwt) {
        return new ResponseEntity<>(userBookService.changeStatusBookEmbeddableId(userBook, jwt), HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('user:read')")
    @DeleteMapping("/{uid}/{id}")
    public HttpStatus deleteUserBook(@PathVariable Long uid, @PathVariable Long id, @RequestHeader("Authorization") String jwt) {
        userBookService.deleteUserBook(new UserBookEmbeddable(uid, id), jwt);
        return HttpStatus.OK;
    }
}
