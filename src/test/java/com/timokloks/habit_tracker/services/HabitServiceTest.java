package com.timokloks.habit_tracker.services;

import com.timokloks.habit_tracker.dtos.CreateHabitRequest;
import com.timokloks.habit_tracker.dtos.HabitResponse;
import com.timokloks.habit_tracker.entities.Habit;
import com.timokloks.habit_tracker.entities.User;
import com.timokloks.habit_tracker.exceptions.UserNotFoundException;
import com.timokloks.habit_tracker.mappers.HabitMapper;
import com.timokloks.habit_tracker.repositories.HabitCompletionRepository;
import com.timokloks.habit_tracker.repositories.HabitRepository;
import com.timokloks.habit_tracker.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HabitServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private HabitMapper habitMapper;

    @Mock
    private HabitRepository habitRepository;

    @Mock
    private HabitCompletionRepository habitCompletionRepository;

    @Mock
    private StreakEngine streakEngine;

    @InjectMocks
    private HabitService habitService;

    // ---------------------------------------
    // createHabit
    // ---------------------------------------

    @Test
    void shouldCreateHabitForExistingUser() {
        //Arrange
        Long userId = 1L;
        User user = new User();
        CreateHabitRequest createHabitRequest = new CreateHabitRequest();
        Habit habit = new Habit();
        HabitResponse habitResponse = new HabitResponse();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(habitMapper.toEntity(createHabitRequest))
                .thenReturn(habit);

        when(habitMapper.toDto(habit))
                .thenReturn(habitResponse);

        //Act
        HabitResponse result = habitService.createHabit(createHabitRequest, userId);

        //Assert
        assertEquals(habit.getUser(), user);
        verify(habitRepository).save(habit);
        assertEquals(habitResponse, result);
    }

    @Test
    void shouldThrowWhenCreatingHabitForUnknownUser() {
        when(userRepository.findById(1L))
            .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            habitService.createHabit(new CreateHabitRequest(), 1L);
        });
    }

    // ---------------------------------------
    // getHabitsOfUser
    // ---------------------------------------

    @Test
    void getHabitsOfUser() {
    }

    @Test
    void deleteHabit() {
    }

    @Test
    void updateHabit() {
    }

    @Test
    void getHabit() {
    }

    @Test
    void getStreak() {
    }
}