package com.timokloks.habit_tracker.services;

import com.timokloks.habit_tracker.entities.HabitCompletion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WeeklyStreakStrategyTest {

    @Mock
    StreakCalculator calculator;

    @InjectMocks
    WeeklyStreakStrategy weeklyStreakStrategy;

    // ---------------------------------------
    // calculateCurrentStreak
    // ---------------------------------------

    @Test
    void currentWeekStreakForEmptyList() {
        List<HabitCompletion> completions = List.of();
        int result = weeklyStreakStrategy.calculateCurrentStreak(completions);
        assertEquals(0, result);
    }

    @Test
    void currentWeekStreakForThreeWeeksInARowStartingFromToday() {
        LocalDateTime completedAt = LocalDateTime.now();
        List<HabitCompletion> completions = List.of(new HabitCompletion(completedAt),
                new HabitCompletion(completedAt.minusWeeks(1)),
                new HabitCompletion(completedAt.minusWeeks(2)));

        int result = weeklyStreakStrategy.calculateCurrentStreak(completions);
        assertEquals(3, result);
    }

    @Test
    void currentWeekStreakForThisWeekNotCompleted() {
        LocalDateTime completedAt = LocalDateTime.now();
        List<HabitCompletion> completions = List.of(new HabitCompletion(completedAt.minusWeeks(1)),
                new HabitCompletion(completedAt.minusWeeks(2)));

        int result = weeklyStreakStrategy.calculateCurrentStreak(completions);
        assertEquals(0, result);
    }

    @Test
    void currentWeekStreakStartingFromTodayWithGap() {
        LocalDateTime completedAt = LocalDateTime.now();
        List<HabitCompletion> completions = List.of(new HabitCompletion(completedAt),
                new HabitCompletion(completedAt.minusWeeks(1)),
                new HabitCompletion(completedAt.minusWeeks(3)));

        int result = weeklyStreakStrategy.calculateCurrentStreak(completions);
        assertEquals(2, result);
    }
}