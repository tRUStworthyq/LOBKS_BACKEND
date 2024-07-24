package com.project.lobks.service;

import com.project.lobks.dto.AuthorDTO;
import com.project.lobks.entity.Author;
import com.project.lobks.repository.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private final AuthorRepository authorRepository;

    @Override
    public List<Author> readAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public Author readAuthorById(Long id) {
        return authorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Author doesn't exist"));
    }

    @Override
    public Author createAuthor(AuthorDTO authorDTO) {
        return authorRepository.save(Author.builder()
                .firstname(authorDTO.getFirstname())
                .lastname(authorDTO.getLastname())
                .build());
    }

    @Override
    public Author updateAuthor(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }

    @Override
    public List<Author> findAuthorsByFirstnameLikeAndLastnameLike(AuthorDTO authorDTO) {
        return authorRepository.findAll().stream()
                .filter(author -> author.getFirstname().toLowerCase().contains(authorDTO.getFirstname().toLowerCase()))
                .filter(author -> author.getLastname().toLowerCase().contains(authorDTO.getLastname().toLowerCase()))
                .toList();

    }

}
