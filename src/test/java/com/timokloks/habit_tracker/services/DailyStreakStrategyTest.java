package com.timokloks.habit_tracker.services;

import com.timokloks.habit_tracker.entities.HabitCompletion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    void currentStreakForEmptyList() {
        List<HabitCompletion> completions = List.of();
        int result = dailyStreakStrategy.calculateCurrentStreak(completions);
        assertEquals(0, result);
    }

    @Test
    void currentStreakForThreeDaysInARowStartingFromToday() {
        LocalDateTime completedAt = LocalDateTime.now();
        List<HabitCompletion> completions = List.of(new HabitCompletion(completedAt),
                new HabitCompletion(completedAt.minusDays(1)),
                new HabitCompletion(completedAt.minusDays(2)));

        int result = dailyStreakStrategy.calculateCurrentStreak(completions);
        assertEquals(3, result);
    }

    @Test
    void currentStreakForTodayNotCompleted() {
        LocalDateTime completedAt = LocalDateTime.now();
        List<HabitCompletion> completions = List.of(new HabitCompletion(completedAt.minusDays(1)),
                new HabitCompletion(completedAt.minusDays(2)));

        int result = dailyStreakStrategy.calculateCurrentStreak(completions);
        assertEquals(0, result);
    }

    @Test
    void currentStreakStartingTodayWithGap() {
        LocalDateTime completedAt = LocalDateTime.now();
        List<HabitCompletion> completions = List.of(new HabitCompletion(completedAt),
                new HabitCompletion(completedAt.minusDays(1)),
                new HabitCompletion(completedAt.minusDays(3)));

        int result = dailyStreakStrategy.calculateCurrentStreak(completions);
        assertEquals(2, result);
    }

}