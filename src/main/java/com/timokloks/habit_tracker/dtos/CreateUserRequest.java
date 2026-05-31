package com.timokloks.habit_tracker.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank(message = "Username must be provided.")
    private String username;
    @Email
    @NotBlank(message = "Email must be provided.")
    private String email;
}
