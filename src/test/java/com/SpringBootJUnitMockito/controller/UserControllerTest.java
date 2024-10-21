package com.SpringBootJUnitMockito.controller;
import com.SpringBootJUnitMockito.entity.User;
import com.SpringBootJUnitMockito.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @Test
    void testGetAllUsers() throws Exception {
        // Arrange
        when(userService.getAllUsers()).thenReturn(Arrays.asList(
                new User(1L, "alex", "alex@gmail.com"),
                new User(2L, "myrah", "myrah@gmail.com")
        ));

        // Act and Assert
        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("alex"))
                .andExpect(jsonPath("$[1].name").value("myrah"));
    }

    @Test
    void testGetUserById() throws Exception {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(Optional.of(new User(1L, "alex", "alex@gmail.com")));

        // Act and Assert
        mockMvc.perform(get("/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("alex"));
    }

    @Test
    void testCreateUser() throws Exception {
        // Arrange
        User user = new User(null, "New User", "newuser@gmail.com");
        when(userService.createUser(Mockito.any(User.class))).thenReturn(new User(1L, "New User", "newuser@example.com"));

        // Act and Assert
        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New User\",\"email\":\"newuser@gmail.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New User"));
    }

    @Test
    void testDeleteUser() throws Exception {
        // Act and Assert
        mockMvc.perform(delete("/v1/users/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }
}
