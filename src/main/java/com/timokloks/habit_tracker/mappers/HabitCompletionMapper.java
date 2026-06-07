package com.timokloks.habit_tracker.mappers;

import com.timokloks.habit_tracker.dtos.HabitCompletionResponse;
import com.timokloks.habit_tracker.entities.HabitCompletion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HabitCompletionMapper {
    HabitCompletionResponse toDto(HabitCompletion habitCompletion);
}
