package com.timokloks.habit_tracker.services;

import com.timokloks.habit_tracker.dtos.CreateUserRequest;
import com.timokloks.habit_tracker.dtos.UserResponse;
import com.timokloks.habit_tracker.entities.User;
import com.timokloks.habit_tracker.exceptions.UserAlreadyExistsException;
import com.timokloks.habit_tracker.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public UserResponse createUser(CreateUserRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException();
        }

        var user = new User(request.getUsername(), request.getEmail());
        userRepository.save(user);

        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt());
    }
}
