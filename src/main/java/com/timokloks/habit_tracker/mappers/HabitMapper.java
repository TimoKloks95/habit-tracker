package com.timokloks.habit_tracker.mappers;

import com.timokloks.habit_tracker.dtos.CreateHabitRequest;
import com.timokloks.habit_tracker.dtos.HabitResponse;
import com.timokloks.habit_tracker.entities.Habit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HabitMapper {
    Habit toEntity(CreateHabitRequest request);

    @Mapping(source = "user.id", target = "userId")
    HabitResponse toDto(Habit entity);
}
