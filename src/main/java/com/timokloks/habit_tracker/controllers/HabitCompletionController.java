package com.timokloks.habit_tracker.controllers;

import com.timokloks.habit_tracker.dtos.HabitCompletionResponse;
import com.timokloks.habit_tracker.entities.HabitCompletion;
import com.timokloks.habit_tracker.mappers.HabitCompletionMapper;
import com.timokloks.habit_tracker.repositories.HabitCompletionRepository;
import com.timokloks.habit_tracker.repositories.HabitRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
@RequestMapping("/habits")
public class HabitCompletionController {

    private final HabitRepository habitRepository;
    private final HabitCompletionRepository habitCompletionRepository;
    private final HabitCompletionMapper habitCompletionMapper;

    @PostMapping("/{habitId}/completions")
    public ResponseEntity<HabitCompletionResponse> createHabitCompletion(@PathVariable Long habitId,
                                                                         UriComponentsBuilder uriBuilder) {
        var habit = habitRepository.findById(habitId).orElse(null);
        if (habit == null) {
            return ResponseEntity.notFound().build();
        }

        var habitCompletion = new HabitCompletion();
        habitCompletion.setHabit(habit);

        habitCompletionRepository.save(habitCompletion);

        var habitCompletionResponse = habitCompletionMapper.toDto(habitCompletion);

        var uri = uriBuilder.path("/completions/{id}").buildAndExpand(habitCompletionResponse.getId()).toUri();
        return ResponseEntity.created(uri).body(habitCompletionResponse);
    }

}
