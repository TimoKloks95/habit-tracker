package com.timokloks.habit_tracker.services;

import com.timokloks.habit_tracker.entities.Frequency;
import com.timokloks.habit_tracker.entities.HabitCompletion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StreakServiceTest {
    @Mock
    private DailyStreakStrategy daily;

    @Mock
    private WeeklyStreakStrategy weekly;

    @Mock
    private MonthlyStreakStrategy monthly;

    @InjectMocks
    private StreakService streakService;

    // ---------------------------------------
    // calculateCurrentStreak
    // ---------------------------------------

    @Test
    void shouldUseDailyStrategyForCurrentStreak() {
        List<HabitCompletion> completions = List.of(new HabitCompletion());

        when(daily.calculateCurrentStreak(completions))
                .thenReturn(1);

        int actual = streakService.calculateCurrentStreak(completions,  Frequency.DAILY);

        assertEquals(1, actual);
        verify(daily).calculateCurrentStreak(completions);
    }

    @Test
    void shouldUseWeeklyStrategyForCurrentStreak() {
        List<HabitCompletion> completions = List.of(new HabitCompletion());

        when(weekly.calculateCurrentStreak(completions))
                .thenReturn(1);

        int actual = streakService.calculateCurrentStreak(completions,  Frequency.WEEKLY);

        assertEquals(1, actual);
        verify(weekly).calculateCurrentStreak(completions);
    }

    @Test
    void shouldUseMonthlyStrategyForCurrentStreak() {
        List<HabitCompletion> completions = List.of(new HabitCompletion());

        when(monthly.calculateCurrentStreak(completions))
                .thenReturn(1);

        int actual = streakService.calculateCurrentStreak(completions,  Frequency.MONTHLY);

        assertEquals(1, actual);
        verify(monthly).calculateCurrentStreak(completions);
    }

    // ---------------------------------------
    // calculateLongestStreak
    // ---------------------------------------

    @Test
    void shouldUseDailyStrategyForLongestStreak() {
        List<HabitCompletion> completions = List.of(new HabitCompletion());

        when(daily.calculateLongestStreak(completions))
                .thenReturn(1);

        int actual = streakService.calculateLongestStreak(completions,  Frequency.DAILY);

        assertEquals(1, actual);
        verify(daily).calculateLongestStreak(completions);
    }

    @Test
    void shouldUseWeeklyStrategyForLongestStreak() {
        List<HabitCompletion> completions = List.of(new HabitCompletion());

        when(weekly.calculateLongestStreak(completions))
                .thenReturn(1);

        int actual = streakService.calculateLongestStreak(completions,  Frequency.WEEKLY);

        assertEquals(1, actual);
        verify(weekly).calculateLongestStreak(completions);
    }

    @Test
    void shouldUseMonthlyStrategyForLongestStreak() {
        List<HabitCompletion> completions = List.of(new HabitCompletion());

        when(monthly.calculateLongestStreak(completions))
                .thenReturn(1);

        int actual = streakService.calculateLongestStreak(completions,  Frequency.MONTHLY);

        assertEquals(1, actual);
        verify(monthly).calculateLongestStreak(completions);
    }

}