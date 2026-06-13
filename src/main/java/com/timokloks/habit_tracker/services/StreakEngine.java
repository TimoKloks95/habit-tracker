package com.timokloks.habit_tracker.services;

import com.timokloks.habit_tracker.dtos.WeekKey;
import com.timokloks.habit_tracker.entities.Frequency;
import com.timokloks.habit_tracker.entities.HabitCompletion;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.Comparator;
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

    public int calculateLongestStreak(List<HabitCompletion> completions, Frequency frequency) {
        return switch(frequency) {
            case DAILY -> calculateLongestDailyStreak(completions);
            case WEEKLY -> calculateLongestWeeklyStreak(completions);
            case MONTHLY -> calculateLongestMonthlyStreak(completions);
        };
    }

    private int calculateLongestMonthlyStreak(List<HabitCompletion> completions) {
        var months = completions.stream()
                .map(c -> YearMonth.from(c.getCompletedAt()))
                .distinct()
                .sorted()
                .toList();

        if (months.isEmpty()) return 0;

        int longest = 1;
        int current = 1;

        for (int i = 1; i < months.size(); i++) {
            YearMonth previous = months.get(i - 1);
            YearMonth today = months.get(i);

            if (previous.plusMonths(1).equals(today)) {
                current++;
                longest = Math.max(longest, current);
            } else {
                current = 1;
            }
        }
        return longest;
    }

    private int calculateLongestWeeklyStreak(List<HabitCompletion> completions) {
        var weeks = completions.stream()
                .map(c -> createWeekKey(c.getCompletedAt().toLocalDate()))
                .distinct()
                .sorted(Comparator
                        .comparingInt(WeekKey::year)
                        .thenComparingInt(WeekKey::week))
                .toList();

        if (weeks.isEmpty()) return 0;

        int longest = 1;
        int current = 1;

        for (int i = 1; i < weeks.size(); i++) {
            WeekKey previous = weeks.get(i - 1);
            WeekKey today = weeks.get(i);

            if (isNextWeek(previous, today)) {
                current++;
                longest = Math.max(longest, current);
            } else {
                current = 1;
            }
        }

        return longest;
    }

    private boolean isNextWeek(WeekKey previous, WeekKey current) {
        WeekFields wf = WeekFields.ISO;

        LocalDate base = LocalDate
                .of(previous.year(), 1, 4)
                .with(wf.weekOfWeekBasedYear(), previous.week());

        LocalDate next = base.plusWeeks(1);

        WeekKey expected = new WeekKey(
                next.get(wf.weekBasedYear()),
                next.get(wf.weekOfWeekBasedYear())
        );

        return expected.equals(current);
    }

    private int calculateLongestDailyStreak(List<HabitCompletion> completions) {
        var days = completions.stream()
                .map(c -> c.getCompletedAt().toLocalDate())
                .distinct()
                .sorted()
                .toList();

        if(days.isEmpty()) return 0;

        int longest = 1;
        int current = 1;

        for (int i = 1; i < days.size(); i++) {
            LocalDate previous = days.get(i - 1);
            LocalDate today = days.get(i);

            if (previous.plusDays(1).equals(today)) {
                current++;
                longest = Math.max(longest, current);
            } else {
                current = 1;
            }
        }
        return longest;
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
        Set<WeekKey> weeks = completions.stream()
                .map(c -> c.getCompletedAt().toLocalDate())
                .map(this::createWeekKey)
                .collect(Collectors.toSet());

        LocalDate current = LocalDate.now();
        WeekKey currentWeek = createWeekKey(current);

        int streak = 0;

        while(weeks.contains(currentWeek)) {
            streak++;
            current = current.minusWeeks(1);
            currentWeek = createWeekKey(current);
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

    private WeekKey createWeekKey(LocalDate date) {
        WeekFields wf = WeekFields.ISO;

        return new WeekKey(
                date.get(wf.weekBasedYear()),
                date.get(wf.weekOfWeekBasedYear())
        );
    }
}
