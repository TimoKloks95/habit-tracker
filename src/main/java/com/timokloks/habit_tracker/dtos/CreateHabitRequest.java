package com.timokloks.habit_tracker.dtos;

import com.timokloks.habit_tracker.entities.Frequency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateHabitRequest {
    @NotBlank(message = "Name must be provided.")
    private String name;

    private String description;

    @NotNull(message = "Frequency must be provided.")
    private Frequency frequency;
}
