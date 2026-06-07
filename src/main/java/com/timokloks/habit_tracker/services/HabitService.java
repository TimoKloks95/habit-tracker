package com.timokloks.habit_tracker.services;

import com.timokloks.habit_tracker.dtos.CreateHabitRequest;
import com.timokloks.habit_tracker.dtos.HabitResponse;
import com.timokloks.habit_tracker.dtos.StreakResponse;
import com.timokloks.habit_tracker.dtos.UpdateHabitRequest;
import com.timokloks.habit_tracker.exceptions.HabitNotFoundException;
import com.timokloks.habit_tracker.exceptions.UserNotFoundException;
import com.timokloks.habit_tracker.mappers.HabitMapper;
import com.timokloks.habit_tracker.repositories.HabitCompletionRepository;
import com.timokloks.habit_tracker.repositories.HabitRepository;
import com.timokloks.habit_tracker.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
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
        var days = completions.stream()
                .map(completion -> completion.getCompletedAt().toLocalDate())
                .collect(Collectors.toSet());

        int streak = 0;
        LocalDate date = LocalDate.now();

        while(days.contains(date)) {
            streak++;
            date = date.minusDays(1);
        }
        return new StreakResponse(streak);
    }
}
