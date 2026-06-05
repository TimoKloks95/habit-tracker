package com.timokloks.habit_tracker.dtos;

import com.timokloks.habit_tracker.entities.Frequency;
import lombok.Data;

@Data
public class UpdateHabitRequest {
    private String name;
    private String description;
    private Frequency frequency;
}
