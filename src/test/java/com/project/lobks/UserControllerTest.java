package com.project.lobks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.lobks.dto.UserDTO;
import com.project.lobks.dto.UserSearchDTO;
import com.project.lobks.dto.UserWithIdDTO;
import com.project.lobks.entity.enums.Role;
import com.project.lobks.entity.enums.StatusUser;
import com.project.lobks.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserServiceImpl userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    UserDTO userDTO = new UserDTO("user1", "user1@mail.ru", StatusUser.ACTIVE, Role.USER);
    UserDTO userDTOUpdated = new UserDTO("user1", "user1@mail.ru", StatusUser.BANNED, Role.USER);
    UserDTO userDTO2 = new UserDTO("user2", "user2@mail.ru", StatusUser.BANNED, Role.USER);
    UserDTO userDTO3 = new UserDTO("user3", "user3@mail.ru", StatusUser.ACTIVE, Role.USER);
    UserSearchDTO userSearchDTO = new UserSearchDTO(StatusUser.ACTIVE);
    UserWithIdDTO userWithIdDTO = new UserWithIdDTO(1L, "user1", "user1@mail.ru", StatusUser.ACTIVE, Role.USER);
    List<UserDTO> findAll = List.of(userDTO, userDTO2, userDTO3);
    List<UserDTO> findByStatusActive = List.of(userDTO, userDTO3);

    @Test
    @WithMockUser(authorities = {"user:read", "user:write"})
    void adminFindAllUsersShouldReturnUsers() throws Exception {
        Mockito.when(userService.findAllUsers()).thenReturn(findAll);

        mvc.perform(get("/api/users/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    @WithMockUser(authorities = "user:read")
    void userFindAllUsersShouldReturnForbiddenStatus() throws Exception {

        mvc.perform(get("/api/users/"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void anonymousFindAllUsersShouldReturnUnauthorizedStatus() throws Exception {

        mvc.perform(get("/api/users/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"user:read", "user:write"})
    void adminFindUserByUsernameShouldReturnUser() throws Exception {
        Mockito.when(userService.findUserByUsername("user1")).thenReturn(userDTO);

        mvc.perform(get("/api/users/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@mail.ru"))
                .andExpect(jsonPath("$.statusUser").value("ACTIVE"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    @WithMockUser(authorities = "user:read")
    void userFindUserByUsernameShouldReturnForbiddenStatus() throws Exception {
        mvc.perform(get("/api/users/user1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void anonymousFindUserByUsernameShouldReturnUnauthorizedStatus() throws Exception {
        mvc.perform(get("/api/users/user1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"user:read", "user:write"})
    void adminFindUserByIdShouldReturnUser() throws Exception {
        Mockito.when(userService.findUserById(1L)).thenReturn(userWithIdDTO);

        mvc.perform(get("/api/users/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@mail.ru"))
                .andExpect(jsonPath("$.statusUser").value("ACTIVE"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    @WithMockUser(authorities = "user:read")
    void userFindUserByIdShouldReturnForbiddenStatus() throws Exception {
        mvc.perform(get("/api/users/id/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void anonymousFindUserByIdShouldReturnUnauthorizedStatus() throws Exception {
        mvc.perform(get("/api/users/id/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"user:read", "user:write"})
    void adminFindUsersByStatusUserShouldReturnUsers() throws Exception {
        Mockito.when(userService.findUsersByStatusUser(userSearchDTO.getStatusUser())).thenReturn(findByStatusActive);

        mvc.perform(post("/api/users/search")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(userSearchDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser(authorities = "user:read")
    void userFindUsersByStatusUserShouldReturnForbiddenStatus() throws Exception {
        mvc.perform(post("/api/users/search")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(userSearchDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void anonymousFindUsersByStatusUserShouldReturnUnauthorizedStatus() throws Exception {
        mvc.perform(post("/api/users/search")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(userSearchDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"user:read", "user:write"})
    void adminUpdateStatusUserByUsernameShouldReturnUser() throws Exception {
        Mockito.when(userService.updateStatusUserByUsername("user1")).thenReturn(userDTOUpdated);

        mvc.perform(patch("/api/users/status/user1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@mail.ru"))
                .andExpect(jsonPath("$.statusUser").value("BANNED"))
                .andExpect(jsonPath("$.role").value("USER"));
    }


    @Test
    @WithMockUser(authorities = "user:read")
    void userUpdateStatusUserByUsernameShouldReturnForbiddenStatus() throws Exception {
        mvc.perform(patch("/api/users/status/user1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void anonymousUpdateStatusUserByUsernameShouldReturnUnauthorizedStatus() throws Exception {
        mvc.perform(patch("/api/users/status/user1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
