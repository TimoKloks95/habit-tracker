package com.timokloks.habit_tracker.services;

import com.timokloks.habit_tracker.dtos.CreateHabitRequest;
import com.timokloks.habit_tracker.dtos.HabitResponse;
import com.timokloks.habit_tracker.dtos.StreakResponse;
import com.timokloks.habit_tracker.dtos.UpdateHabitRequest;
import com.timokloks.habit_tracker.entities.Frequency;
import com.timokloks.habit_tracker.entities.Habit;
import com.timokloks.habit_tracker.entities.HabitCompletion;
import com.timokloks.habit_tracker.entities.User;
import com.timokloks.habit_tracker.exceptions.HabitNotFoundException;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    private StreakService streakService;

    @InjectMocks
    private HabitService habitService;

    // ---------------------------------------
    // createHabit
    // ---------------------------------------

    @Test
    void shouldCreateHabitForExistingUser() {
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

        HabitResponse result = habitService.createHabit(createHabitRequest, userId);

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
    void shouldGetHabitsForExistingUser() {
        Long userId = 1L;
        Habit h1 = new Habit();
        Habit h2 = new Habit();
        HabitResponse hr1 = new HabitResponse();
        HabitResponse hr2 = new HabitResponse();
        User user = new User();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(habitRepository.findByUser(user))
            .thenReturn(List.of(h1, h2));

        when(habitMapper.toDto(h1))
                .thenReturn(hr1);

        when(habitMapper.toDto(h2))
            .thenReturn(hr2);

        List<HabitResponse> actual = habitService.getHabitsOfUser(userId);

        assertThat(actual)
                .containsExactly(hr1, hr2);
    }

    @Test
    void shouldThrowWhenGettingHabitsForUnknownUser() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            habitService.getHabitsOfUser(1L);
        });
    }

    // --------------------------------------
    // deleteHabit
    // --------------------------------------

    @Test
    void shouldDeleteHabitWhenHabitExists() {
        Habit habit = new Habit();

        when(habitRepository.findById(1L))
                .thenReturn(Optional.of(habit));

        habitService.deleteHabit(1L);

        verify(habitRepository).delete(habit);
    }

    @Test
    void shouldThrowWhenHabitDoesNotExistForDeleteHabit() {
        when(habitRepository.findById(1L))
            .thenReturn(Optional.empty());

        assertThrows(HabitNotFoundException.class, () -> habitService.deleteHabit(1L));
    }

    // --------------------------------------
    // updateHabit
    // --------------------------------------

    @Test
    void shouldUpdateWhenHabitExists() {
        Long habitId = 1L;
        Habit habit = new Habit();
        habit.setDescription("Old description");
        habit.setName("Old name");

        UpdateHabitRequest updateHabitRequest = new UpdateHabitRequest();
        updateHabitRequest.setDescription("New description");
        updateHabitRequest.setName("New name");

        HabitResponse habitResponse = new HabitResponse();

        when(habitRepository.findById(habitId))
            .thenReturn(Optional.of(habit));

        doNothing().when(habitMapper).update(any(), any());

        when(habitMapper.toDto(habit))
                .thenReturn(habitResponse);

        HabitResponse actual = habitService.updateHabit(habitId, updateHabitRequest);

        assertEquals(habitResponse, actual);
        verify(habitRepository).save(habit);
        verify(habitMapper).update(updateHabitRequest, habit);
    }

    @Test
    void shouldThrowWhenHabitDoesNotExistForUpdateHabit() {
        Long habitId = 1L;
        UpdateHabitRequest request = new UpdateHabitRequest();
        when(habitRepository.findById(habitId))
                .thenReturn(Optional.empty());

        assertThrows(HabitNotFoundException.class, () -> habitService.updateHabit(habitId, request));
    }

    // --------------------------------------
    // getHabit
    // --------------------------------------

    @Test
    void shouldReturnHabitWhenHabitExists() {
        Long habitId = 1L;
        Habit habit = new Habit();
        HabitResponse response = new HabitResponse();

        when(habitRepository.findById(habitId))
            .thenReturn(Optional.of(habit));

        when(habitMapper.toDto(habit))
            .thenReturn(response);

        HabitResponse actual = habitService.getHabit(habitId);

        assertEquals(response, actual);
    }

    @Test
    void shouldThrowWhenHabitDoesNotExistForGetHabit() {
        Long habitId = 1L;
        when(habitRepository.findById(1L))
            .thenReturn(Optional.empty());

        assertThrows(HabitNotFoundException.class, () -> habitService.getHabit(habitId));
    }

    // --------------------------------------
    // getStreak
    // --------------------------------------

    @Test
    void shouldGetStreakDataWhenHabitExists() {
        Long habitId = 1L;
        Habit habit = new Habit();
        habit.setFrequency(Frequency.DAILY);
        List<HabitCompletion> completions = List.of(new HabitCompletion(), new HabitCompletion());
        int currentStreak = 0;
        int longestStreak = 0;

        when(habitRepository.findById(habitId))
            .thenReturn(Optional.of(habit));

        when(habitCompletionRepository.findByHabit(habit))
            .thenReturn(completions);

        when(streakService.calculateCurrentStreak(completions, Frequency.DAILY))
            .thenReturn(currentStreak);

        when(streakService.calculateLongestStreak(completions, Frequency.DAILY))
            .thenReturn(longestStreak);

        StreakResponse actual = habitService.getStreak(habitId);

        assertEquals(currentStreak, actual.getCurrentStreak());
        assertEquals(longestStreak, actual.getLongestStreak());
    }

    @Test
    void shouldThrowWhenHabitDoesNotExistForGetStreak() {
        Long habitId = 1L;
        when(habitRepository.findById(habitId))
            .thenReturn(Optional.empty());

        assertThrows(HabitNotFoundException.class, () -> habitService.getStreak(habitId));
    }
}