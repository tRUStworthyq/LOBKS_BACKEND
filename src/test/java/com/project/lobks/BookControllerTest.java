package com.project.lobks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.lobks.controller.BookController;
import com.project.lobks.dto.BookDTO;
import com.project.lobks.dto.BookUpdateDTO;
import com.project.lobks.entity.Author;
import com.project.lobks.entity.Book;
import com.project.lobks.service.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    BookServiceImpl bookService;


    private final Book book = new Book(1, "1234", "1234", new Author(1L, "Maxim", "Dolgiy"));
    private final Book book2 = new Book(1, "4321", "4321", new Author(2L, "Svetlana", "Molotkova"));
    private final BookDTO bookDTO = new BookDTO("1234", "1234", 1L);
    private final BookUpdateDTO bookUpdateDTO = new BookUpdateDTO(1L, "1234", "1234", 1L);
    private final List<Book> books = List.of(book, book2);
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Test
    void readAllBooksShouldReturnAllBooks() throws Exception {
        Mockito.when(this.bookService.readAllBooks()).thenReturn(books);

        mvc.perform(get("/api/books/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void readBookByIdShouldReturnBook() throws Exception {
        Mockito.when(this.bookService.readBookById(1L)).thenReturn(book);

        mvc.perform(get("/api/books/book/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("1234"))
                .andExpect(jsonPath("$.status").value("1234"))
                .andExpect(jsonPath("$.author.id").value(1))
                .andExpect(jsonPath("$.author.firstname").value("Maxim"))
                .andExpect(jsonPath("$.author.lastname").value("Dolgiy"));
    }

    @Test
    void readAllBooksByAuthorIdShouldReturnListOfBooks() throws Exception {
        Mockito.when(this.bookService.readAllBooksByAuthorId(1L)).thenReturn(List.of(book));

        mvc.perform(get("/api/books/all/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

    }

    @Test
    void createBookShouldReturnBook() throws Exception {
        Mockito.when(this.bookService.createBook(bookDTO)).thenReturn(book);

        mvc.perform(post("/api/books/create")
                        .content(objectMapper.writeValueAsString(bookDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("1234"))
                .andExpect(jsonPath("$.status").value("1234"))
                .andExpect(jsonPath("$.author.id").value(1))
                .andExpect(jsonPath("$.author.firstname").value("Maxim"))
                .andExpect(jsonPath("$.author.lastname").value("Dolgiy"))
                .andReturn();
    }

    @Test
    void updateBookShouldReturnBook() throws Exception {
        Mockito.when(this.bookService.updateBook(bookUpdateDTO)).thenReturn(book);

        mvc.perform(put("/api/books/update")
                        .content(objectMapper.writeValueAsString(bookUpdateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("1234"))
                .andExpect(jsonPath("$.status").value("1234"))
                .andExpect(jsonPath("$.author.id").value(1))
                .andExpect(jsonPath("$.author.firstname").value("Maxim"))
                .andExpect(jsonPath("$.author.lastname").value("Dolgiy"))
                .andReturn();
    }

    @Test
    void deleteBookShouldReturnOkStatus() throws Exception {
        mvc.perform(delete("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
