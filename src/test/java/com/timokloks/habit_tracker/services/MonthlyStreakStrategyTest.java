package com.timokloks.habit_tracker.services;

import com.timokloks.habit_tracker.entities.HabitCompletion;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MonthlyStreakStrategyTest {

    @Mock
    private StreakCalculator streakCalculator;

    @InjectMocks
    private MonthlyStreakStrategy monthlyStreakStrategy;

    // ---------------------------------------
    // calculateCurrentStreak
    // ---------------------------------------

    @Test
    void currentMonthStreakForEmptyList() {
        List<HabitCompletion> completions = List.of();
        int result = monthlyStreakStrategy.calculateCurrentStreak(completions);
        assertEquals(0, result);
    }

    @Test
    void currentMonthStreakForThreeMonthsInARowStartingFromToday() {
        LocalDateTime completedAt = LocalDateTime.now();
        List<HabitCompletion> completions = List.of(new HabitCompletion(completedAt),
                new HabitCompletion(completedAt.minusMonths(1)),
                new HabitCompletion(completedAt.minusMonths(2)));

        int result = monthlyStreakStrategy.calculateCurrentStreak(completions);
        assertEquals(3, result);
    }

    @Test
    void currentMonthStreakForThisMonthNotCompleted() {
        LocalDateTime completedAt = LocalDateTime.now();
        List<HabitCompletion> completions = List.of(new HabitCompletion(completedAt.minusMonths(1)),
                new HabitCompletion(completedAt.minusMonths(2)));

        int result = monthlyStreakStrategy.calculateCurrentStreak(completions);
        assertEquals(0, result);
    }

    @Test
    void currentMonthStreakStartingFromTodayWithGap() {
        LocalDateTime completedAt = LocalDateTime.now();
        List<HabitCompletion> completions = List.of(new HabitCompletion(completedAt),
                new HabitCompletion(completedAt.minusMonths(1)),
                new HabitCompletion(completedAt.minusMonths(3)));

        int result = monthlyStreakStrategy.calculateCurrentStreak(completions);
        assertEquals(2, result);
    }

    // ---------------------------------------
    // calculateLongestStreak
    // ---------------------------------------

    @Test
    void calculateLongestStreak() {
        LocalDateTime now = LocalDateTime.now();
        List<HabitCompletion> completions = List.of(new HabitCompletion(now),
                new HabitCompletion(now.minusMonths(1)),
                new HabitCompletion(now.minusMonths(2)));

        List<YearMonth> expectedMonths = Stream.of(YearMonth.from(now),
                        YearMonth.from(now.minusMonths(1)),
                        YearMonth.from(now.minusMonths(2)))
                .sorted()
                .toList();

        when(streakCalculator.getLongestStreak(eq(expectedMonths), any()))
                .thenReturn(3);

        int result = monthlyStreakStrategy.calculateLongestStreak(completions);

        assertEquals(3, result);
        verify(streakCalculator).getLongestStreak(eq(expectedMonths), ArgumentMatchers.any());
    }
}