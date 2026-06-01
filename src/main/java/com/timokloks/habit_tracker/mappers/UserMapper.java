package com.timokloks.habit_tracker.mappers;

import com.timokloks.habit_tracker.dtos.UserResponse;
import com.timokloks.habit_tracker.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toDto(User user);
}
