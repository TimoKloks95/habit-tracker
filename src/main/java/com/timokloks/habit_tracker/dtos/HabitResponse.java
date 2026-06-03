package com.timokloks.habit_tracker.dtos;

import com.timokloks.habit_tracker.entities.Frequency;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HabitResponse {
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private Frequency frequency;
    private LocalDateTime createdAt;
}
