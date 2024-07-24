package com.project.lobks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.lobks.controller.AuthorController;
import com.project.lobks.dto.AuthorDTO;
import com.project.lobks.entity.Author;
import com.project.lobks.service.AuthorServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    AuthorServiceImpl authorService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    Author author1 = new Author(1L, "Maxim", "Dolgiy");
    Author author2 = new Author(2L, "Svetlana", "Molotkova");
    Author author3 = new Author(3L, "Vlad", "Baryshnikov");

    AuthorDTO authorDTO = new AuthorDTO("Maxim", "Dolgiy");
    AuthorDTO authorDTO2 = new AuthorDTO("a", "ol");

    List<Author> authors = List.of(author1, author2, author3);



    @Test
    void readAllAuthorsShouldReturnAllAuthors() throws Exception {
        Mockito.when(authorService.readAllAuthors()).thenReturn(authors);

        mvc.perform(get("/api/authors/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void readAuthorByIdShouldReturnAuthor() throws Exception {
        Mockito.when(authorService.readAuthorById(1L)).thenReturn(author1);

        mvc.perform(get("/api/authors/author/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstname").value("Maxim"))
                .andExpect(jsonPath("$.lastname").value("Dolgiy"));
    }

    @Test
    void findAuthorsByFirstnameContainingAndLastnameContainingShouldReturnAuthors() throws Exception {
        Mockito.when(authorService.findAuthorsByFirstnameLikeAndLastnameLike(authorDTO2))
                .thenReturn(List.of(author1, author2));

        mvc.perform(post("/api/authors/search")
                        .content(objectMapper.writeValueAsString(authorDTO2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void createAuthorShouldReturnAuthor() throws Exception {
        Mockito.when(authorService.createAuthor(authorDTO)).thenReturn(author1);

        mvc.perform(post("/api/authors/create")
                .content(objectMapper.writeValueAsString(authorDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("Maxim"))
                .andExpect(jsonPath("$.lastname").value("Dolgiy"))
                .andReturn();
    }

    @Test
    void updateAuthorShouldReturnAuthor() throws Exception {
        Mockito.when(authorService.updateAuthor(author1)).thenReturn(author1);

        mvc.perform(put("/api/authors/update")
                .content(objectMapper.writeValueAsString(author1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstname").value("Maxim"))
                .andExpect(jsonPath("$.lastname").value("Dolgiy"))
                .andReturn();
    }

    @Test
    void deleteAuthorShouldReturnOkStatus() throws Exception {
        mvc.perform(delete("/api/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
