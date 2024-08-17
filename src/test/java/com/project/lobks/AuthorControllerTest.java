package com.project.lobks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.lobks.controller.AuthorController;
import com.project.lobks.dto.AuthorDTO;
import com.project.lobks.entity.Author;
import com.project.lobks.service.AuthorServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private AuthorServiceImpl authorService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    Author author1 = new Author(1L, "Maxim", "Dolgiy");
    Author author2 = new Author(2L, "Svetlana", "Molotkova");
    Author author3 = new Author(3L, "Vlad", "Baryshnikov");

    AuthorDTO authorDTO = new AuthorDTO("Maxim", "Dolgiy");
    AuthorDTO authorDTO2 = new AuthorDTO("a", "ol");

    List<Author> authors = List.of(author1, author2, author3);



    @Test
    @WithMockUser(authorities = "user:read")
    void userReadAllAuthorsShouldReturnAllAuthors() throws Exception {
        Mockito.when(authorService.readAllAuthors()).thenReturn(authors);

        mvc.perform(get("/api/authors/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    @WithAnonymousUser
    void anonymousReadAllAuthorsShouldReturnUnauthorizedStatus() throws Exception {
        mvc.perform(get("/api/authors/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "user:read")
    void userReadAuthorByIdShouldReturnAuthor() throws Exception {
        Mockito.when(authorService.readAuthorById(1L)).thenReturn(author1);

        mvc.perform(get("/api/authors/author/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstname").value("Maxim"))
                .andExpect(jsonPath("$.lastname").value("Dolgiy"));
    }

    @Test
    @WithAnonymousUser
    void anonymousReadAuthorByIdShouldReturnUnauthorizedStatus() throws Exception {
        mvc.perform(get("/api/authors/author/1"))
                .andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser(authorities = "user:read")
    void userFindAuthorsByFirstnameContainingAndLastnameContainingShouldReturnAuthors() throws Exception {
        Mockito.when(authorService.findAuthorsByFirstnameLikeAndLastnameLike(authorDTO2))
                .thenReturn(List.of(author1, author2));

        mvc.perform(post("/api/authors/search")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(authorDTO2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithAnonymousUser
    void anonymousFindAuthorsByFirstnameContainingAndLastnameContainingShouldReturnUnauthorizedStatus() throws Exception {
        mvc.perform(post("/api/authors/search")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(authorDTO2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"user:read", "user:write"})
    void adminCreateAuthorShouldReturnAuthor() throws Exception {
        Mockito.when(authorService.createAuthor(authorDTO)).thenReturn(author1);

        mvc.perform(post("/api/authors/create")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(authorDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("Maxim"))
                .andExpect(jsonPath("$.lastname").value("Dolgiy"))
                .andReturn();
    }

    @Test
    @WithMockUser(authorities = "user:read")
    void userCreateAuthorShouldReturnForbiddenStatus() throws Exception {
        mvc.perform(post("/api/authors/create")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(authorDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void anonymousCreateAuthorShouldReturnUnauthorizedStatus() throws Exception {
        mvc.perform(post("/api/authors/create")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(authorDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"user:read", "user:write"})
    void adminUpdateAuthorShouldReturnAuthor() throws Exception {
        Mockito.when(authorService.updateAuthor(author1)).thenReturn(author1);

        mvc.perform(put("/api/authors/update")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(author1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("Maxim"))
                .andExpect(jsonPath("$.lastname").value("Dolgiy"))
                .andReturn();
    }

    @Test
    @WithMockUser(authorities = "user:read")
    void userUpdateAuthorShouldReturnForbiddenStatus() throws Exception {
        mvc.perform(put("/api/authors/update")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(author1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void anonymousUpdateAuthorShouldReturnUnauthorizedStatus() throws Exception {
        mvc.perform(put("/api/authors/update")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(author1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"user:read", "user:write"})
    void adminDeleteAuthorShouldReturnOkStatus() throws Exception {
        mvc.perform(delete("/api/authors/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "user:read")
    void userDeleteAuthorShouldReturnForbiddenStatus() throws Exception {
        mvc.perform(delete("/api/authors/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void anonymousDeleteAuthorShouldReturnUnauthorizedStatus() throws Exception {
        mvc.perform(delete("/api/authors/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
