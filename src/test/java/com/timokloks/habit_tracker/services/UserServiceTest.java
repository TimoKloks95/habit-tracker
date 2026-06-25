package com.timokloks.habit_tracker.services;

import com.timokloks.habit_tracker.dtos.CreateUserRequest;
import com.timokloks.habit_tracker.dtos.UserResponse;
import com.timokloks.habit_tracker.entities.User;
import com.timokloks.habit_tracker.exceptions.UserAlreadyExistsException;
import com.timokloks.habit_tracker.exceptions.UserNotFoundException;
import com.timokloks.habit_tracker.mappers.UserMapper;
import com.timokloks.habit_tracker.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    // ---------------------------------------
    // createUser
    // ---------------------------------------

    @Test
    void shouldCreateUserWhenUserDoesNotExist() {
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("test@test.com");
        request.setUsername("test");
        UserResponse response = new UserResponse();

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(userMapper.toDto(any(User.class)))
                .thenReturn(response);

        UserResponse actual = userService.createUser(request);

        assertEquals(response, actual);

        verify(userRepository)
                .save(argThat(user ->
                        user.getEmail().equals(request.getEmail()) && user.getUsername().equals(request.getUsername())
                ));
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() {
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("test@test.com");

        when(userRepository.existsByEmail(request.getEmail()))
            .thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(request));
    }

    // ---------------------------------------
    // getUser
    // ---------------------------------------

    @Test
    void shouldGetUserWhenUserExists() {
        Long userId = 1L;
        User user = new User();
        UserResponse response = new UserResponse();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(userMapper.toDto(user))
                .thenReturn(response);

        UserResponse actual = userService.getUser(userId);

        assertEquals(response, actual);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        Long userId = 1L;

        when(userRepository.findById(userId))
            .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(userId));
    }
}