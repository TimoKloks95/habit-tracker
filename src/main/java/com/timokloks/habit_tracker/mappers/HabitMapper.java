package com.timokloks.habit_tracker.mappers;

import com.timokloks.habit_tracker.dtos.CreateHabitRequest;
import com.timokloks.habit_tracker.dtos.HabitResponse;
import com.timokloks.habit_tracker.entities.Habit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HabitMapper {
    Habit toEntity(CreateHabitRequest request);

    HabitResponse toDto(Habit entity);
}
