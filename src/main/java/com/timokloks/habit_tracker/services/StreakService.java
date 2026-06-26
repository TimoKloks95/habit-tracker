package com.timokloks.habit_tracker.services;

import com.timokloks.habit_tracker.dtos.WeekKey;
import com.timokloks.habit_tracker.entities.Frequency;
import com.timokloks.habit_tracker.entities.HabitCompletion;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StreakService {
    private final DailyStreakStrategy daily;
    private final WeeklyStreakStrategy weekly;
    private final MonthlyStreakStrategy monthly;

    public StreakStrategy getStreakStrategy(Frequency frequency) {
        return switch (frequency) {
            case DAILY -> daily;
            case WEEKLY -> weekly;
            case MONTHLY -> monthly;
        };
    }

    public int calculateCurrentStreak(List<HabitCompletion> completions, Frequency frequency) {getStreakStrategy(frequency);
        return getStreakStrategy(frequency).calculateCurrentStreak(completions);
    }

    public int calculateLongestStreak(List<HabitCompletion> completions, Frequency frequency) {
        return getStreakStrategy(frequency).calculateLongestStreak(completions);
    }
}
