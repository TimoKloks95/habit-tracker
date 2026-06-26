package com.timokloks.habit_tracker.services;

import com.timokloks.habit_tracker.dtos.WeekKey;
import com.timokloks.habit_tracker.entities.HabitCompletion;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class WeeklyStreakStrategy implements StreakStrategy {
    private StreakCalculator streakCalculator;

    @Override
    public int calculateCurrentStreak(List<HabitCompletion> completions) {
        Set<WeekKey> weeks = completions.stream()
                .map(c -> c.getCompletedAt().toLocalDate())
                .map(this::createWeekKey)
                .collect(Collectors.toSet());

        LocalDate current = LocalDate.now();
        WeekKey currentWeek = createWeekKey(current);

        int streak = 0;

        while(weeks.contains(currentWeek)) {
            streak++;
            currentWeek = previousWeek(currentWeek);
        }

        return streak;
    }

    @Override
    public int calculateLongestStreak(List<HabitCompletion> completions) {
        var weeks = completions.stream()
                .map(c -> createWeekKey(c.getCompletedAt().toLocalDate()))
                .distinct()
                .sorted(Comparator
                        .comparingInt(WeekKey::year)
                        .thenComparingInt(WeekKey::week))
                .toList();

        return streakCalculator.getLongestStreak(weeks, this::isNextWeek);
    }

    private WeekKey createWeekKey(LocalDate date) {
        WeekFields wf = WeekFields.ISO;

        return new WeekKey(
                date.get(wf.weekBasedYear()),
                date.get(wf.weekOfWeekBasedYear())
        );
    }

    private boolean isNextWeek(WeekKey previous, WeekKey current) {
        return toLocalDate(previous).plusWeeks(1).equals(toLocalDate(current));
    }

    private LocalDate toLocalDate(WeekKey key) {
        WeekFields wf = WeekFields.ISO;
        return LocalDate.of(key.year(), 1, 4)
                .with(wf.weekOfWeekBasedYear(), key.week());

    }

    private WeekKey previousWeek(WeekKey key) {
        LocalDate date = toLocalDate(key).minusWeeks(1);
        return createWeekKey(date);
    }
}
