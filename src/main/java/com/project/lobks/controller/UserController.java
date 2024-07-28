package com.project.lobks.controller;

import com.project.lobks.dto.UserDTO;
import com.project.lobks.dto.UserSearchDTO;
import com.project.lobks.dto.UserWithIdDTO;
import com.project.lobks.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> findAllUsers() {
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> findUserByUsername(@PathVariable String username) {
        return new ResponseEntity<>(userService.findUserByUsername(username), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/id/{id}")
    public ResponseEntity<UserWithIdDTO> findUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findUserById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping("/search")
    public ResponseEntity<List<UserDTO>> findUsersByStatusUser(@RequestBody UserSearchDTO userSearchDTO) {
        return new ResponseEntity<>(userService.findUsersByStatusUser(userSearchDTO.getStatusUser()), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PatchMapping("/status/{username}")
    public ResponseEntity<UserDTO> updateStatusUserByUsername(@PathVariable String username) {
        return new ResponseEntity<>(userService.updateStatusUserByUsername(username), HttpStatus.OK);
    }
}
