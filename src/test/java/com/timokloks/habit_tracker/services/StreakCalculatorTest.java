package com.timokloks.habit_tracker.services;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.*;

class StreakCalculatorTest {

    private final StreakCalculator streakCalculator = new StreakCalculator();
    private final BiPredicate<Integer, Integer> consecutiveInts =
            (a, b) -> b == a + 1;

    private final BiPredicate<LocalDate, LocalDate> consecutiveDates =
            (a, b) -> a.plusDays(1).equals(b);

    @Test
    void longestStreakForEmptyList() {
        List<Integer> input = List.of();
        int result = streakCalculator.getLongestStreak(input, consecutiveInts);
        assertEquals(0, result);
    }

    @Test
    void longestStreakForUninterruptedStreakInt() {
        List<Integer> input = List.of(1, 2, 3, 4, 5);
        int result = streakCalculator.getLongestStreak(input, consecutiveInts);
        assertEquals(5, result);
    }

    @Test
    void longestStreakForInterruptedStreakFirstPartLongerInt() {
        List<Integer> input = List.of(1, 2, 3, 4, 6, 7);
        int result = streakCalculator.getLongestStreak(input, consecutiveInts);
        assertEquals(4, result);
    }

    @Test
    void longestStreakForInterruptedStreakLastPartLongerInt() {
        List<Integer> input = List.of(1, 2, 4, 5, 6, 7);
        int result = streakCalculator.getLongestStreak(input, consecutiveInts);
        assertEquals(4, result);
    }

    @Test
    void longestStreakForUninterruptedStreakLocalDate() {
        List<LocalDate> input = List.of(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 2),
                LocalDate.of(2026, 1, 3)
        );
        int result = streakCalculator.getLongestStreak(input, consecutiveDates);
        assertEquals(3, result);
    }

    @Test
    void longestStreakForInterruptedStreakFirstPartLongerLocalDate() {
        List<LocalDate> input = List.of(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 2),
                LocalDate.of(2026, 1, 3),
                LocalDate.of(2026, 1, 4),
                LocalDate.of(2026, 1, 6),
                LocalDate.of(2026, 1, 7)
        );
        int result = streakCalculator.getLongestStreak(input, consecutiveDates);
        assertEquals(4, result);
    }

    @Test
    void longestStreakForInterruptedStreakLastPartLongerLocalDate() {
        List<LocalDate> input = List.of(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 2),
                LocalDate.of(2026, 1, 4),
                LocalDate.of(2026, 1, 5),
                LocalDate.of(2026, 1, 6),
                LocalDate.of(2026, 1, 7)
        );
        int result = streakCalculator.getLongestStreak(input, consecutiveDates);
        assertEquals(4, result);
    }
}