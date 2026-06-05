package com.timokloks.habit_tracker.controllers;

import com.timokloks.habit_tracker.dtos.ErrorDto;
import com.timokloks.habit_tracker.dtos.HabitCompletionResponse;
import com.timokloks.habit_tracker.exceptions.HabitCompletionNotFoundException;
import com.timokloks.habit_tracker.exceptions.HabitNotFoundException;
import com.timokloks.habit_tracker.services.HabitCompletionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

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

    @GetMapping("/{habitId}/completions")
    public List<HabitCompletionResponse> getHabitCompletionsOfHabit(@PathVariable Long habitId) {
        return habitCompletionService.getHabitCompletionsOfHabit(habitId);
    }

    @DeleteMapping("/{habitId}/completions/{completionId}")
    public ResponseEntity<Void> deleteHabitCompletion(@PathVariable("habitId") Long habitId, @PathVariable("completionId") Long completionId) {
        habitCompletionService.deleteHabitCompletion(habitId, completionId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(HabitNotFoundException.class)
    public ResponseEntity<ErrorDto> handleHabitNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorDto("Habit not found.")
        );
    }

    @ExceptionHandler(HabitCompletionNotFoundException.class)
    public ResponseEntity<ErrorDto> handleHabitCompletionNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorDto("Habit completion not found.")
        );
    }
}
