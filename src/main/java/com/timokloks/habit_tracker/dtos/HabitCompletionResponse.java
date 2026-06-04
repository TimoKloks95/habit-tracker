package com.timokloks.habit_tracker.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HabitCompletionResponse {
    private Long id;
    private Long habitId;
    private LocalDateTime completedAt;
}
