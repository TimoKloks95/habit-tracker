package com.timokloks.habit_tracker.controllers;

import com.timokloks.habit_tracker.dtos.CreateUserRequest;
import com.timokloks.habit_tracker.dtos.UserCreatedResponse;
import com.timokloks.habit_tracker.entities.User;
import com.timokloks.habit_tracker.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<UserCreatedResponse> createUser(@Valid @RequestBody CreateUserRequest request, UriComponentsBuilder uriBuilder) {
        if(userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        var user = new User(request.getUsername(), request.getEmail());
        userRepository.save(user);

        var userDto = new UserCreatedResponse(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt());
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }
}
