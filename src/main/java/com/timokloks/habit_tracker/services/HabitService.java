package com.timokloks.habit_tracker.services;

import com.timokloks.habit_tracker.dtos.*;
import com.timokloks.habit_tracker.entities.Frequency;
import com.timokloks.habit_tracker.entities.HabitCompletion;
import com.timokloks.habit_tracker.exceptions.HabitNotFoundException;
import com.timokloks.habit_tracker.exceptions.UserNotFoundException;
import com.timokloks.habit_tracker.mappers.HabitMapper;
import com.timokloks.habit_tracker.repositories.HabitCompletionRepository;
import com.timokloks.habit_tracker.repositories.HabitRepository;
import com.timokloks.habit_tracker.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HabitService {

    private final UserRepository userRepository;
    private final HabitMapper habitMapper;
    private final HabitRepository habitRepository;
    private final HabitCompletionRepository habitCompletionRepository;

    public HabitResponse createHabit(CreateHabitRequest request, Long userId) {
        var user = userRepository.findById(userId).orElse(null);
        if(user == null) {
           throw new UserNotFoundException();
        }

        var habit = habitMapper.toEntity(request);
        habit.setUser(user);

        habitRepository.save(habit);

        return habitMapper.toDto(habit);
    }

    public List<HabitResponse> getHabitsOfUser(Long userId) {
        var user = userRepository.findById(userId).orElse(null);
        if(user == null) {
            throw new UserNotFoundException();
        }

        var habits = habitRepository.findByUser(user);
        return habits.stream()
                .map(habitMapper::toDto)
                .toList();
    }

    public void deleteHabit(Long habitId) {
        var habit = habitRepository.findById(habitId).orElse(null);
        if(habit == null) {
            throw new HabitNotFoundException();
        }
        habitRepository.delete(habit);
    }

    public HabitResponse updateHabit(Long habitId, UpdateHabitRequest request) {
        var habit = habitRepository.findById(habitId).orElse(null);
        if(habit == null) {
            throw new HabitNotFoundException();
        }
        habitMapper.update(request, habit);
        habitRepository.save(habit);
        return habitMapper.toDto(habit);
    }

    public HabitResponse getHabit(Long habitId) {
        var habit = habitRepository.findById(habitId).orElse(null);
        if(habit == null) {
            throw new HabitNotFoundException();
        }
        return habitMapper.toDto(habit);
    }

    public StreakResponse getStreak(Long habitId) {
        var habit = habitRepository.findById(habitId).orElse(null);
        if(habit == null) {
            throw new HabitNotFoundException();
        }
        var completions = habitCompletionRepository.findByHabit(habit);

        int streak = calculateStreak(completions, habit.getFrequency());
        return new StreakResponse(streak);
    }

    private int calculateStreak(List<HabitCompletion> completions, Frequency frequency) {
        switch(frequency) {
            case DAILY -> {
                return calculateDailyStreak(completions);
            }
            case WEEKLY -> {
                return calculateWeeklyStreak(completions);
            }
            case MONTHLY -> {
                return calculateMonthlyStreak(completions);
            }
            default -> {
                return 0;
            }
        }
    }

    private int calculateMonthlyStreak(List<HabitCompletion> completions) {
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

    private int calculateWeeklyStreak(List<HabitCompletion> completions) {
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

    private int calculateDailyStreak(List<HabitCompletion> completions) {
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
