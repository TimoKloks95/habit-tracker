package com.timokloks.habit_tracker.services;

import com.timokloks.habit_tracker.dtos.WeekKey;
import com.timokloks.habit_tracker.entities.Frequency;
import com.timokloks.habit_tracker.entities.HabitCompletion;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class StreakEngine {
    public int calculateCurrentStreak(List<HabitCompletion> completions, Frequency frequency) {
        return switch(frequency) {
            case DAILY -> calculateCurrentDailyStreak(completions);
            case WEEKLY -> calculateCurrentWeeklyStreak(completions);
            case MONTHLY -> calculateCurrentMonthlyStreak(completions);
        };
    }

    private int calculateCurrentMonthlyStreak(List<HabitCompletion> completions) {
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

    private int calculateCurrentWeeklyStreak(List<HabitCompletion> completions) {
        WeekFields wf = WeekFields.ISO;

        Set<WeekKey> weeks = completions.stream()
                .map(c -> c.getCompletedAt().toLocalDate())
                .map(date -> new WeekKey(
                        date.get(wf.weekBasedYear()),
                        date.get(wf.weekOfWeekBasedYear())
                ))
                .collect(Collectors.toSet());

        LocalDate current = LocalDate.now();

        WeekKey currentWeek = new WeekKey(
                current.get(wf.weekBasedYear()),
                current.get(wf.weekOfWeekBasedYear())
        );

        int streak = 0;

        while(weeks.contains(currentWeek)) {
            streak++;
            current = current.minusWeeks(1);

            currentWeek = new WeekKey(
                    current.get(wf.weekBasedYear()),
                    current.get(wf.weekOfWeekBasedYear())
            );
        }

        return streak;
    }

    private int calculateCurrentDailyStreak(List<HabitCompletion> completions) {
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
}
