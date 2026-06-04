package com.timokloks.habit_tracker.controllers;

import com.timokloks.habit_tracker.dtos.HabitCompletionResponse;
import com.timokloks.habit_tracker.exceptions.HabitNotFoundException;
import com.timokloks.habit_tracker.services.HabitCompletionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
@RequestMapping("/habits")
public class HabitCompletionController {

    private final HabitCompletionService habitCompletionService;

    @PostMapping("/{habitId}/completions")
    public ResponseEntity<HabitCompletionResponse> createHabitCompletion(@PathVariable Long habitId,
                                                                         UriComponentsBuilder uriBuilder) {
        var habitCompletionResponse = habitCompletionService.createHabitCompletion(habitId);
        var uri = uriBuilder.path("/completions/{id}").buildAndExpand(habitCompletionResponse.getId()).toUri();
        return ResponseEntity.created(uri).body(habitCompletionResponse);
    }

    @ExceptionHandler(HabitNotFoundException.class)
    public ResponseEntity<Void> handleHabitNotFound() {
        return ResponseEntity.notFound().build();
    }
}
