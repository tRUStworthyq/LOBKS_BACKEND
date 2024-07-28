package com.project.lobks.service;

import com.project.lobks.dto.UserDTO;
import com.project.lobks.dto.UserWithIdDTO;
import com.project.lobks.entity.enums.StatusUser;

import java.util.List;

public interface UserService {
    List<UserDTO> findAllUsers();

    UserDTO findUserByUsername(String username);

    List<UserDTO> findUsersByStatusUser(StatusUser statusUser);

    UserDTO updateStatusUserByUsername(String username);

    UserWithIdDTO findUserById(Long id);
}