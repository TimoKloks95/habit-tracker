package com.timokloks.habit_tracker.services;

import com.timokloks.habit_tracker.dtos.HabitCompletionResponse;
import com.timokloks.habit_tracker.entities.Habit;
import com.timokloks.habit_tracker.entities.HabitCompletion;
import com.timokloks.habit_tracker.exceptions.HabitCompletionNotFoundException;
import com.timokloks.habit_tracker.exceptions.HabitNotFoundException;
import com.timokloks.habit_tracker.mappers.HabitCompletionMapper;
import com.timokloks.habit_tracker.repositories.HabitCompletionRepository;
import com.timokloks.habit_tracker.repositories.HabitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HabitCompletionServiceTest {

    @Mock
    private HabitRepository habitRepository;

    @Mock
    private HabitCompletionRepository habitCompletionRepository;

    @Mock
    private HabitCompletionMapper habitCompletionMapper;

    @InjectMocks
    private HabitCompletionService habitCompletionService;

    // ---------------------------------------
    // createHabitCompletion
    // ---------------------------------------

    @Test
    void shouldCreateHabitCompletionForExistingHabit() {
        Long habitId = 1L;
        Habit habit = new Habit();
        HabitCompletionResponse response = new HabitCompletionResponse();

        when(habitRepository.findById(habitId))
                .thenReturn(Optional.of(habit));

        when(habitCompletionMapper.toDto(any(HabitCompletion.class)))
                .thenReturn(response);

        HabitCompletionResponse actual = habitCompletionService.createHabitCompletion(habitId);

        verify(habitCompletionRepository)
                .save(argThat(completion ->
                        completion.getHabit() == habit
                ));

        assertEquals(response, actual);
    }

    @Test
    void shouldThrowExceptionWhenHabitNotFoundForCreateHabitCompletion() {
        Long habitId = 1L;
        when(habitRepository.findById(habitId))
            .thenReturn(Optional.empty());

        assertThrows(HabitNotFoundException.class, () -> habitCompletionService.createHabitCompletion(habitId));
    }

    // ---------------------------------------
    // getHabitCompletions
    // ---------------------------------------

    @Test
    void shouldGetHabitCompletionsForExistingHabit() {
        Long habitId = 1L;
        Habit habit = new Habit();
        HabitCompletion habitCompletion1 = new HabitCompletion();
        HabitCompletion habitCompletion2 = new HabitCompletion();
        HabitCompletionResponse habitCompletionResponse1 = new HabitCompletionResponse();
        HabitCompletionResponse habitCompletionResponse2 = new HabitCompletionResponse();

        when(habitRepository.findById(habitId))
                .thenReturn(Optional.of(habit));

        when(habitCompletionRepository.findByHabit(habit))
                .thenReturn(List.of(habitCompletion1, habitCompletion2));

        when(habitCompletionMapper.toDto(habitCompletion1))
            .thenReturn(habitCompletionResponse1);

        when(habitCompletionMapper.toDto(habitCompletion2))
            .thenReturn(habitCompletionResponse2);

        List<HabitCompletionResponse> actual = habitCompletionService.getHabitCompletions(habitId);

        assertEquals(habitCompletionResponse1, actual.getFirst());
        assertEquals(habitCompletionResponse2, actual.getLast());
    }

    @Test
    void shouldThrowExceptionWhenHabitNotFoundForGetHabitCompletion() {
        Long habitId = 1L;
        when(habitRepository.findById(habitId))
            .thenReturn(Optional.empty());

        assertThrows(HabitNotFoundException.class, () -> habitCompletionService.getHabitCompletions(habitId));
    }

    // ---------------------------------------
    // deleteHabitCompletion
    // ---------------------------------------

    @Test
    void shouldDeleteHabitCompletionForExistingHabitAndHabitCompletion() {
        Long habitId = 1L;
        Long habitCompletionId = 1L;
        Habit habit = new Habit();
        HabitCompletion habitCompletion = new HabitCompletion();

        when(habitRepository.findById(habitId))
            .thenReturn(Optional.of(habit));

        when(habitCompletionRepository.findByHabitAndId(habit, habitCompletionId))
                .thenReturn(Optional.of(habitCompletion));

        habitCompletionService.deleteHabitCompletion(habitId, habitCompletionId);

        verify(habitCompletionRepository).delete(habitCompletion);
    }

    @Test
    void shouldThrowExceptionWhenHabitNotFoundForDeleteHabitCompletion() {
        Long habitId = 1L;
        when(habitRepository.findById(habitId))
            .thenReturn(Optional.empty());

        assertThrows(HabitNotFoundException.class, () -> habitCompletionService.deleteHabitCompletion(habitId, 1L));
    }

    @Test
    void shouldThrowExceptionWhenHabitCompletionNotFoundForDeleteHabitCompletion() {
        Long habitId = 1L;
        Long habitCompletionId = 1L;
        Habit habit = new Habit();

        when(habitRepository.findById(habitId))
            .thenReturn(Optional.of(habit));

        when(habitCompletionRepository.findByHabitAndId(habit, habitCompletionId))
            .thenReturn(Optional.empty());

        assertThrows(HabitCompletionNotFoundException.class, () -> habitCompletionService.deleteHabitCompletion(habitId, habitCompletionId));
    }

    // ---------------------------------------
    // deleteHabitCompletions
    // ---------------------------------------

    @Test
    void shouldDeleteHabitCompletionForExistingHabit() {
        Long habitId = 1L;
        Habit habit = new Habit();

        when(habitRepository.findById(habitId))
            .thenReturn(Optional.of(habit));

        habitCompletionService.deleteHabitCompletions(habitId);

        verify(habitCompletionRepository).deleteByHabit(habit);
    }

    @Test
    void shouldThrowExceptionWhenHabitNotFoundForDeleteHabitCompletions() {
        Long habitId = 1L;
        when(habitRepository.findById(habitId))
            .thenReturn(Optional.empty());

        assertThrows(HabitNotFoundException.class, () -> habitCompletionService.deleteHabitCompletions(habitId));
    }
}