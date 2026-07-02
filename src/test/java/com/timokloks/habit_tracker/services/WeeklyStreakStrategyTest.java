package com.timokloks.habit_tracker.services;

import com.timokloks.habit_tracker.dtos.WeekKey;
import com.timokloks.habit_tracker.entities.HabitCompletion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeeklyStreakStrategyTest {

    @Mock
    StreakCalculator streakCalculator;

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

    // ---------------------------------------
    // calculateLongestStreak
    // ---------------------------------------

    @Test
    void longestWeeklyStreakWithHappyPath() {
        LocalDateTime now = LocalDateTime.now();
        List<HabitCompletion> completions = List.of(new HabitCompletion(now),
                new HabitCompletion(now.minusWeeks(1)),
                new HabitCompletion(now.minusWeeks(2)));

        WeekFields wf = WeekFields.ISO;

        WeekKey firstKey =  new WeekKey(
                now.get(wf.weekBasedYear()),
                now.get(wf.weekOfWeekBasedYear())
        );

        WeekKey secondKey =  new WeekKey(
                now.minusWeeks(1).get(wf.weekBasedYear()),
                now.minusWeeks(1).get(wf.weekOfWeekBasedYear())
        );

        WeekKey thirdKey =  new WeekKey(
                now.minusWeeks(2).get(wf.weekBasedYear()),
                now.minusWeeks(2).get(wf.weekOfWeekBasedYear())
        );

        List<WeekKey> expectedKeys = Stream.of(firstKey, secondKey, thirdKey)
                .sorted(Comparator
                        .comparingInt(WeekKey::year)
                        .thenComparingInt(WeekKey::week))
                .toList();

        when(streakCalculator.getLongestStreak(eq(expectedKeys), any()))
                .thenReturn(3);

        int result = weeklyStreakStrategy.calculateLongestStreak(completions);

        assertEquals(3, result);
        verify(streakCalculator).getLongestStreak(eq(expectedKeys), ArgumentMatchers.any());
    }
}