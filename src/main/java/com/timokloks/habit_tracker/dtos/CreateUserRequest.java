package com.timokloks.habit_tracker.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank(message = "Username must be provided.")
    public String username;
    @Email
    public String email;
}
