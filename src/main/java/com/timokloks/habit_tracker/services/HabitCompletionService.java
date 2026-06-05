package com.timokloks.habit_tracker.services;

import com.timokloks.habit_tracker.dtos.HabitCompletionResponse;
import com.timokloks.habit_tracker.entities.HabitCompletion;
import com.timokloks.habit_tracker.exceptions.HabitCompletionNotFoundException;
import com.timokloks.habit_tracker.exceptions.HabitNotFoundException;
import com.timokloks.habit_tracker.mappers.HabitCompletionMapper;
import com.timokloks.habit_tracker.repositories.HabitCompletionRepository;
import com.timokloks.habit_tracker.repositories.HabitRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HabitCompletionService {
    private final HabitRepository habitRepository;
    private final HabitCompletionRepository habitCompletionRepository;
    private final HabitCompletionMapper habitCompletionMapper;

    public HabitCompletionResponse createHabitCompletion(Long habitId) {
        var habit = habitRepository.findById(habitId).orElse(null);
        if (habit == null) {
            throw new HabitNotFoundException();
        }

        var habitCompletion = new HabitCompletion();
        habitCompletion.setHabit(habit);

        habitCompletionRepository.save(habitCompletion);
        return habitCompletionMapper.toDto(habitCompletion);
    }

    public List<HabitCompletionResponse> getHabitCompletionsOfHabit(Long habitId) {
        var habit = habitRepository.findById(habitId).orElse(null);
        if (habit == null) {
            throw new HabitNotFoundException();
        }

        var habitCompletions = habitCompletionRepository.findByHabit(habit);

        return habitCompletions.stream()
                .map(habitCompletionMapper::toDto)
                .toList();
    }

    public void deleteHabitCompletion(Long habitId, Long completionId) {
        var habit = habitRepository.findById(habitId).orElseThrow(HabitNotFoundException::new);
        var habitCompletion = habitCompletionRepository.findByHabitAndId(habit, completionId).orElseThrow(HabitCompletionNotFoundException::new);
        habitCompletionRepository.delete(habitCompletion);
    }
}
