package com.project.lobks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.project.lobks.dto.UserBookCreateDTO;
import com.project.lobks.dto.UserBookDTO;
import com.project.lobks.dto.UserDTO;
import com.project.lobks.dto.jwt.request.LoginRequest;
import com.project.lobks.entity.Author;
import com.project.lobks.entity.Book;
import com.project.lobks.entity.UserBook;
import com.project.lobks.entity.UserBookEmbeddable;
import com.project.lobks.entity.enums.Role;
import com.project.lobks.entity.enums.StatusBook;
import com.project.lobks.entity.enums.StatusUser;
import com.project.lobks.service.UserBookServiceImpl;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserBookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private String jwt;

    @MockBean
    private UserBookServiceImpl userBookService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    UserBookCreateDTO userBookCreateDTO = new UserBookCreateDTO(new Book(1L, "aaa", "desc", new Author(1L, "ccc", "ddd")), StatusBook.PLANS);
    UserDTO userDTO = new UserDTO("user", "user@mail.ru", StatusUser.ACTIVE, Role.USER);

    UserBook userBook = new UserBook(new UserBookEmbeddable(1L, 1L), StatusBook.PLANS);
    @PostConstruct
    public void setup() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/api/auth/signin")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content(objectMapper.writeValueAsString(new LoginRequest("user", "user")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        jwt = "Bearer " + JsonPath.parse(mvcResult.getResponse().getContentAsString())
                .read("$.jwt");
    }

    @Test
    @WithMockUser(authorities = "user:read")
    void userFindBooksByUserIdShouldReturnListOfUserBookCreateDTO() throws Exception {
        Mockito.when(userBookService.findBooksByUserId(1L)).thenReturn(List.of(userBookCreateDTO));

        mvc.perform(get("/api/user_books/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithAnonymousUser
    void anonymousFindBooksByUserIdShouldReturnUnauthorizedStatus() throws Exception {
        mvc.perform(get("/api/user_books/books/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"user:read", "user:write"})
    void adminFindUsersByBookIdShouldReturnListOfUserDTO() throws Exception {
        Mockito.when(userBookService.findUsersByBookId(1L)).thenReturn(List.of(userDTO));

        mvc.perform(get("/api/user_books/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(authorities = "user:read")
    void userFindUsersByBookIdShouldReturnForbiddenStatus() throws Exception {
        mvc.perform(get("/api/user_books/users/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void anonymousFindUsersByBookIdShouldReturnUnauthorizedStatus() throws Exception {
        mvc.perform(get("/api/user_books/users/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "user:read")
    void userSaveUserBookShouldReturnUserBookCreateDTO() throws Exception {
        Mockito.when(userBookService.saveUserBook(new UserBookDTO(1L, 1L), jwt)).thenReturn(userBookCreateDTO);

        mvc.perform(post("/api/user_books/save")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(new UserBookDTO(1L, 1L)))
                        .header("Authorization", jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser(authorities = "user:read")
    void userDeleteUserBookShouldReturnOkStatus() throws Exception {
        mvc.perform(delete("/api/user_books/1/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .header("Authorization", jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void anonymousDeleteUserBookShouldReturnBadRequestStatus() throws Exception {
        mvc.perform(delete("/api/user_books/1/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = "user:read")
    void userChangeStatusBookEmbeddableIdShouldReturnStatusBook() throws Exception {
        Mockito.when(userBookService.changeStatusBookEmbeddableId(userBook, jwt)).thenReturn(userBook.getStatusBook());

        mvc.perform(patch("/api/user_books/status")
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .header("Authorization", jwt)
                    .content(objectMapper.writeValueAsString(userBook))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
// Try to set expected value to StatusBook.PLANS :)
                .andExpect(jsonPath("$").value("PLANS"));

    }

    @Test
    @WithAnonymousUser
    void anonymousChangeStatusBookEmbeddableIdShouldReturnBadRequestStatus() throws Exception {
        mvc.perform(patch("/api/user_books/status")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(userBook))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
