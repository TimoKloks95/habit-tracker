package com.timokloks.habit_tracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserCreatedResponse {
    private Long id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
}
