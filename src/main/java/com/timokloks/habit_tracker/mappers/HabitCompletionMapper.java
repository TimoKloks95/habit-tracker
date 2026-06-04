package com.timokloks.habit_tracker.mappers;

import com.timokloks.habit_tracker.dtos.HabitCompletionResponse;
import com.timokloks.habit_tracker.entities.HabitCompletion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HabitCompletionMapper {
    @Mapping(source = "habit.id", target = "habitId")
    HabitCompletionResponse toDto(HabitCompletion habitCompletion);
}
