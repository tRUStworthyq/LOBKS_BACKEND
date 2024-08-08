package com.project.lobks.service;

import com.project.lobks.dto.UserBookCreateDTO;
import com.project.lobks.dto.UserBookDTO;
import com.project.lobks.dto.UserDTO;
import com.project.lobks.entity.User;
import com.project.lobks.entity.UserBook;
import com.project.lobks.entity.UserBookEmbeddable;
import com.project.lobks.entity.enums.StatusBook;
import com.project.lobks.repository.BookRepository;
import com.project.lobks.repository.UserBookRepository;
import com.project.lobks.repository.UserRepository;
import com.project.lobks.security.jwt.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class UserBookServiceImpl implements UserBookService {

    @Autowired
    private UserBookRepository userBookRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;

    @Override
    public List<UserBookCreateDTO> findBooksByUserId(Long id) {
        List<UserBook> userBooks = userBookRepository.findAll().stream()
                .filter(userBook -> userBook.getUserBookEmbeddable().getUserId().equals(id))
                .toList();
        return userBooks.stream()
                .map(userBook -> new UserBookCreateDTO(bookRepository.findById(userBook.getUserBookEmbeddable().getBookId()).get(), userBook.getStatusBook()))
                .toList();
    }

    @Override
    public List<UserDTO> findUsersByBookId(Long id) {
        List<Long> users_ids = userBookRepository.findAll().stream()
                .filter(userBook -> userBook.getUserBookEmbeddable().getBookId().equals(id))
                .map(userBook -> userBook.getUserBookEmbeddable().getUserId())
                .toList();
        return userRepository.findAll().stream()
                .filter(user -> users_ids.contains(user.getId()))
                .map(user -> new UserDTO(
                        user.getUsername(),
                        user.getEmail(),
                        user.getStatusUser(),
                        user.getRole()
                ))
                .toList();
    }

    @Override
    public UserBookCreateDTO saveUserBook(UserBookDTO userBookDTO, String jwt) {
        if (userBookDTO.getUser_id().equals(getIdFromJwt(jwt))) {
            UserBook userBook = new UserBook(new UserBookEmbeddable(
                    userBookDTO.getUser_id(),
                    userBookDTO.getBook_id()
            ), StatusBook.PLANS);
            userBookRepository.save(userBook);

            return new UserBookCreateDTO(
                    bookRepository.findById(userBookDTO.getBook_id()).get(),
                    StatusBook.PLANS
            );
        }
        throw new SecurityException("attempt to access someone else's list");
    }

    @Override
    public void deleteUserBook(UserBookEmbeddable userBookEmbeddable, String jwt) {
        if (userBookEmbeddable.getUserId().equals(getIdFromJwt(jwt))) {
            userBookRepository.deleteById(userBookEmbeddable);
        }
    }

    @Override
    public StatusBook changeStatusBookEmbeddableId(UserBook userBook, String jwt) {
        if (userBook.getUserBookEmbeddable().getUserId().equals(getIdFromJwt(jwt))) {
            Optional<UserBook> optionalUserBook = userBookRepository.findById(userBook.getUserBookEmbeddable());
            if (optionalUserBook.isPresent()) {
                UserBook newUserBook = optionalUserBook.get();
                newUserBook.setStatusBook(userBook.getStatusBook());
                userBookRepository.save(newUserBook);
                return userBook.getStatusBook();
            }
            throw new EntityNotFoundException("userBook doesn't exists");
        }
        throw new SecurityException("attempt to access someone else's list");
    }

    private Long getIdFromJwt(String jwt) {
        if (StringUtils.hasText(jwt) && jwt.startsWith("Bearer ")) {
            String token = jwt.substring(7);
            if (jwtService.validateToken(token)) {
                Optional<User> optionalUser = userRepository
                        .findUserByUsername(jwtService.getUsername(token));
                if (optionalUser.isPresent()) {
                    return optionalUser.get().getId();
                }
            }
        }
        throw new JwtException("jwt is invalid");
    }
}
