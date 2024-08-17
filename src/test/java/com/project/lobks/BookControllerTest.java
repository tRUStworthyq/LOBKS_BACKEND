package com.project.lobks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.lobks.config.SecurityConfig;
import com.project.lobks.controller.BookController;
import com.project.lobks.dto.BookDTO;
import com.project.lobks.dto.BookUpdateDTO;
import com.project.lobks.entity.Author;
import com.project.lobks.entity.Book;
import com.project.lobks.service.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private BookServiceImpl bookService;

    private final Book book = new Book(1, "1234", "1234", new Author(1L, "Maxim", "Dolgiy"));
    private final Book book2 = new Book(1, "4321", "4321", new Author(2L, "Svetlana", "Molotkova"));
    private final BookDTO bookDTO = new BookDTO("1234", "1234", 1L);
    private final BookUpdateDTO bookUpdateDTO = new BookUpdateDTO(1L, "1234", "1234", 1L);
    private final List<Book> books = List.of(book, book2);
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Test
    @WithMockUser(authorities = "user:read")
    void userReadAllBooksShouldReturnAllBooks() throws Exception {
        Mockito.when(this.bookService.readAllBooks()).thenReturn(books);

        mvc.perform(get("/api/books/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithAnonymousUser
    void anonymousReadAllBooksShouldReturnUnauthorizedStatus() throws Exception {
        mvc.perform(get("/api/books/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "user:read")
    void userReadBookByIdShouldReturnBook() throws Exception {
        Mockito.when(this.bookService.readBookById(1L)).thenReturn(book);

        mvc.perform(get("/api/books/book/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("1234"))
                .andExpect(jsonPath("$.description").value("1234"))
                .andExpect(jsonPath("$.author.id").value(1))
                .andExpect(jsonPath("$.author.firstname").value("Maxim"))
                .andExpect(jsonPath("$.author.lastname").value("Dolgiy"));
    }

    @Test
    @WithAnonymousUser
    void anonymousReadBookByIdShouldReturnUnauthorizedStatus() throws Exception {
        mvc.perform(get("/api/books/book/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "user:read")
    void userReadAllBooksByAuthorIdShouldReturnListOfBooks() throws Exception {
        Mockito.when(this.bookService.readAllBooksByAuthorId(1L)).thenReturn(List.of(book));

        mvc.perform(get("/api/books/all/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

    }

    @Test
    @WithAnonymousUser
    void anonymousReadAllBooksByAuthorIdShouldReturnUnauthorizedStatus() throws Exception {
        mvc.perform(get("/api/books/all/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"user:read", "user:write"})
    void adminCreateBookShouldReturnBook() throws Exception {
        Mockito.when(bookService.createBook(bookDTO)).thenReturn(book);

        mvc.perform(post("/api/books/create")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(bookDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("1234"))
                .andExpect(jsonPath("$.description").value("1234"))
                .andExpect(jsonPath("$.author.id").value(1))
                .andExpect(jsonPath("$.author.firstname").value("Maxim"))
                .andExpect(jsonPath("$.author.lastname").value("Dolgiy"));
    }
    @Test
    @WithMockUser(authorities = "user:read")
    void userCreateBookShouldReturnForbiddenStatus() throws Exception {
        mvc.perform(post("/api/books/create")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(bookDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void anonymousCreateBookShouldReturnUnauthorizedStatus() throws Exception {
        mvc.perform(post("/api/books/create")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(bookDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser(authorities = {"user:read", "user:write"})
    void adminUpdateBookShouldReturnBook() throws Exception {
        Mockito.when(this.bookService.updateBook(bookUpdateDTO)).thenReturn(book);

        mvc.perform(put("/api/books/update")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(bookUpdateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("1234"))
                .andExpect(jsonPath("$.description").value("1234"))
                .andExpect(jsonPath("$.author.id").value(1))
                .andExpect(jsonPath("$.author.firstname").value("Maxim"))
                .andExpect(jsonPath("$.author.lastname").value("Dolgiy"));
    }


    @Test
    @WithMockUser(authorities = "user:read")
    void userUpdateBookShouldReturnForbiddenStatus() throws Exception {
        mvc.perform(put("/api/books/update")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(bookUpdateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void anonymousUpdateBookShouldReturnUnauthorizedStatus() throws Exception {
        mvc.perform(put("/api/books/update")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(bookUpdateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"user:read", "user:write"})
    void adminDeleteBookShouldReturnOkStatus() throws Exception {
        mvc.perform(delete("/api/books/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "user:read")
    void userDeleteBookShouldReturnForbiddenStatus() throws Exception {
        mvc.perform(delete("/api/books/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
    @Test
    @WithAnonymousUser
    void anonymousDeleteBookShouldReturnUnauthorizedStatus() throws Exception {
        mvc.perform(delete("/api/books/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
