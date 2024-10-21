package com.SpringBootJUnitMockito.service;
import com.SpringBootJUnitMockito.entity.User;
import com.SpringBootJUnitMockito.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        List<User> users = Arrays.asList(new User(1L, "alex", "alex@gmail.com"),
                new User(2L, "sarah", "sarah@gmail.com"));
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
        // Arrange
        User user = new User(1L, "alex", "alex@gmail.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUserById(1L);

        // Assert
        assertEquals("alex", result.get().getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateUser() {
        // Arrange
        User user = new User(null, "New User", "newuser@example.com");
        when(userRepository.save(user)).thenReturn(new User(1L, "New User", "newuser@example.com"));

        // Act
        User result = userService.createUser(user);

        // Assert
        assertEquals(1L, result.getId());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDeleteUser() {
        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository, times(1)).deleteById(1L);
    }
}
