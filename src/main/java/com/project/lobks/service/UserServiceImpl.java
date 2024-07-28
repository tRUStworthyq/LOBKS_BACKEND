package com.project.lobks.service;

import com.project.lobks.dto.UserDTO;
import com.project.lobks.dto.UserWithIdDTO;
import com.project.lobks.entity.User;
import com.project.lobks.entity.enums.Role;
import com.project.lobks.entity.enums.StatusUser;
import com.project.lobks.repository.BookRepository;
import com.project.lobks.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Override
    public List<UserDTO> findAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream().filter(user -> user.getRole().equals(Role.USER))
                .map(user -> new UserDTO(user.getUsername(), user.getEmail(),
                user.getStatusUser(), user.getRole())).toList();
    }

    @Override
    public UserDTO findUserByUsername(String username) {
        Optional<User> optionalUser = userRepository.findUserByUsername(username);
        if (optionalUser.isPresent() && optionalUser.get().getRole().equals(Role.USER)) {
            User user = optionalUser.get();
            return new UserDTO(user.getUsername(), user.getEmail(), user.getStatusUser(), user.getRole());
        }
        throw new EntityNotFoundException("User with username=" + username + " doesn't exists");
    }

    @Override
    public List<UserDTO> findUsersByStatusUser(StatusUser statusUser) {
        Optional<List<User>> optionalUsers = userRepository.findUsersByStatusUser(statusUser);
        if (optionalUsers.isPresent()) {
            List<User> users = optionalUsers.get();

            return users.stream().filter(user -> user.getRole().equals(Role.USER))
                    .map(user -> new UserDTO(user.getUsername(), user.getEmail(), statusUser, user.getRole()))
                    .toList();
        }
        throw new EntityNotFoundException("Users with statusUser=" + statusUser + " doesn't exist");
    }

    @Override
    public UserDTO updateStatusUserByUsername(String username) {
        Optional<User> optionalUser = userRepository.findUserByUsername(username);
        if (optionalUser.isPresent() && optionalUser.get().getRole().equals(Role.USER)) {
            User user = optionalUser.get();
            if (user.getStatusUser().equals(StatusUser.ACTIVE)) {
                user.setStatusUser(StatusUser.BANNED);
            } else {
                user.setStatusUser(StatusUser.ACTIVE);
            }
            userRepository.save(user);
            return new UserDTO(username, user.getEmail(), user.getStatusUser(), user.getRole());
        }
        throw new EntityNotFoundException("User with username=" + username + " doesn't exists");
    }

    @Override
    public UserWithIdDTO findUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new UserWithIdDTO(
                    id,
                    user.getUsername(),
                    user.getEmail(),
                    user.getStatusUser(),
                    user.getRole()
            );
        }
        throw new EntityNotFoundException("User with id=" + id + " doesn't exists");
    }
}
