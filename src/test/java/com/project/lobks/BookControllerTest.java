package com.project.lobks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.lobks.dto.BookDTO;
import com.project.lobks.entity.Book;
import com.project.lobks.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class BookControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    BookService bookService;

    private final Book book = new Book(1, "1234", "1234", "begane");
    private final Book book2 = new Book(1, "4321", "4321", "finished");
    private final BookDTO bookDTO = new BookDTO("1234", "1234", "begane");
    private final List<Book> books = new ArrayList<>(Collections.singleton(book));
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Test
    void readAllBooksShouldReturnAllBooks() throws Exception {
        Mockito.when(this.bookService.readAllBooks()).thenReturn(books);

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void readBookByIdShouldReturnBook() throws Exception {
        Mockito.when(this.bookService.readBookById(1)).thenReturn(book);

        mvc.perform(get("/book/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("1234"))
                .andExpect(jsonPath("$.author").value("1234"))
                .andExpect(jsonPath("$.status").value("begane"));
    }

    @Test
    void createBookShouldReturnBook() throws Exception {
        Mockito.when(this.bookService.createBook(bookDTO)).thenReturn(book);

        mvc.perform(post("/create")
                        .content(objectMapper.writeValueAsString(bookDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("1234"))
                .andExpect(jsonPath("$.author").value("1234"))
                .andExpect(jsonPath("$.status").value("begane"))
                .andReturn();
    }

    @Test
    void updateBookShouldReturnBook() throws Exception {
        Mockito.when(this.bookService.updateBook(book)).thenReturn(book2);

        mvc.perform(put("/update")
                        .content(objectMapper.writeValueAsString(book))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("4321"))
                .andExpect(jsonPath("$.author").value("4321"))
                .andExpect(jsonPath("$.status").value("finished"))
                .andReturn();
    }

    @Test
    void deleteBookShouldOkStatus() throws Exception {
        mvc.perform(delete("/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
