package com.timokloks.habit_tracker.services;

import com.timokloks.habit_tracker.entities.HabitCompletion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DailyStreakStrategyTest {

    @Mock
    private StreakCalculator streakCalculator;

    @InjectMocks
    private DailyStreakStrategy dailyStreakStrategy;

    // ---------------------------------------
    // calculateCurrentStreak
    // ---------------------------------------

    @Test
    void currentDailyStreakForEmptyList() {
        List<HabitCompletion> completions = List.of();
        int result = dailyStreakStrategy.calculateCurrentStreak(completions);
        assertEquals(0, result);
    }

    @Test
    void currentDailyStreakForThreeDaysInARowStartingFromToday() {
        LocalDateTime completedAt = LocalDateTime.now();
        List<HabitCompletion> completions = List.of(new HabitCompletion(completedAt),
                new HabitCompletion(completedAt.minusDays(1)),
                new HabitCompletion(completedAt.minusDays(2)));

        int result = dailyStreakStrategy.calculateCurrentStreak(completions);
        assertEquals(3, result);
    }

    @Test
    void currentDailyStreakForTodayNotCompleted() {
        LocalDateTime completedAt = LocalDateTime.now();
        List<HabitCompletion> completions = List.of(new HabitCompletion(completedAt.minusDays(1)),
                new HabitCompletion(completedAt.minusDays(2)));

        int result = dailyStreakStrategy.calculateCurrentStreak(completions);
        assertEquals(0, result);
    }

    @Test
    void currentDailyStreakStartingTodayWithGap() {
        LocalDateTime completedAt = LocalDateTime.now();
        List<HabitCompletion> completions = List.of(new HabitCompletion(completedAt),
                new HabitCompletion(completedAt.minusDays(1)),
                new HabitCompletion(completedAt.minusDays(3)));

        int result = dailyStreakStrategy.calculateCurrentStreak(completions);
        assertEquals(2, result);
    }

    // ---------------------------------------
    // calculateLongestStreak
    // ---------------------------------------

    @Test
    void longestDailyStreakWithHappyPath() {
        LocalDateTime firstDate = LocalDateTime.of(2026, 1, 1, 1, 1);
        LocalDateTime secondDate = LocalDateTime.of(2026, 1, 2, 1, 2);
        LocalDateTime thirdDate = LocalDateTime.of(2026, 1, 3, 1, 2);
        List<HabitCompletion> completions = List.of(new HabitCompletion(firstDate), new HabitCompletion(secondDate),
                new HabitCompletion(thirdDate));

        List<LocalDate> expectedDays = List.of(firstDate.toLocalDate(), secondDate.toLocalDate(), thirdDate.toLocalDate());

        when(streakCalculator.getLongestStreak(eq(expectedDays), ArgumentMatchers.any()))
                .thenReturn(3);

        int actual = dailyStreakStrategy.calculateLongestStreak(completions);
        assertEquals(3, actual);
        verify(streakCalculator).getLongestStreak(eq(expectedDays), ArgumentMatchers.any());
    }
}