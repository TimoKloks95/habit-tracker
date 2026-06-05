package com.timokloks.habit_tracker.mappers;

import com.timokloks.habit_tracker.dtos.CreateHabitRequest;
import com.timokloks.habit_tracker.dtos.HabitResponse;
import com.timokloks.habit_tracker.dtos.UpdateHabitRequest;
import com.timokloks.habit_tracker.entities.Habit;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.BeanMapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface HabitMapper {
    Habit toEntity(CreateHabitRequest request);
    HabitResponse toDto(Habit entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(UpdateHabitRequest request, @MappingTarget Habit habit);
}
