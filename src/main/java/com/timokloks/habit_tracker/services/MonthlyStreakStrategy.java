package com.timokloks.habit_tracker.services;

import com.timokloks.habit_tracker.entities.HabitCompletion;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class MonthlyStreakStrategy implements StreakStrategy {
    private StreakCalculator calculator;

    @Override
    public int calculateCurrentStreak(List<HabitCompletion> completions) {
        Set<YearMonth> months = completions.stream()
                .map(c -> YearMonth.from(c.getCompletedAt()))
                .collect(Collectors.toSet());

        YearMonth current = YearMonth.from(LocalDate.now());

        int streak = 0;

        while(months.contains(current)) {
            streak++;
            current = current.minusMonths(1);
        }

        return streak;
    }

    @Override
    public int calculateLongestStreak(List<HabitCompletion> completions) {
        var months = completions.stream()
                .map(c -> YearMonth.from(c.getCompletedAt()))
                .distinct()
                .sorted()
                .toList();

        return calculator.getLongestStreak(months, (prev, curr) -> prev.plusMonths(1).equals(curr));
    }
}
