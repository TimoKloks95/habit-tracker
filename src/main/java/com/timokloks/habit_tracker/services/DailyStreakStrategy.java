package com.timokloks.habit_tracker.services;

import com.timokloks.habit_tracker.entities.HabitCompletion;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class DailyStreakStrategy implements StreakStrategy {
    private StreakCalculator streakCalculator;

    @Override
    public int calculateCurrentStreak(List<HabitCompletion> completions) {
        var days = completions.stream()
                .map(completion -> completion.getCompletedAt().toLocalDate())
                .collect(Collectors.toSet());

        int streak = 0;
        LocalDate date = LocalDate.now();

        while(days.contains(date)) {
            streak++;
            date = date.minusDays(1);
        }
        return streak;
    }

    @Override
    public int calculateLongestStreak(List<HabitCompletion> completions) {
        var days = completions.stream()
                .map(c -> c.getCompletedAt().toLocalDate())
                .distinct()
                .sorted()
                .toList();

        return streakCalculator.getLongestStreak(days, (prev, curr) -> prev.plusDays(1).equals(curr));
    }
}
