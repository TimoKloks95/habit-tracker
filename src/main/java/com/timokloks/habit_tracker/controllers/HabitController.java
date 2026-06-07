package com.timokloks.habit_tracker.controllers;

import com.timokloks.habit_tracker.dtos.*;
import com.timokloks.habit_tracker.exceptions.HabitNotFoundException;
import com.timokloks.habit_tracker.exceptions.UserNotFoundException;
import com.timokloks.habit_tracker.services.HabitService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
public class HabitController
{
    private final HabitService habitService;

    @PostMapping("/users/{userId}/habits")
    public ResponseEntity<HabitResponse> createHabit(@Valid @RequestBody CreateHabitRequest request,
                                                     @PathVariable Long userId,
                                                     UriComponentsBuilder uriBuilder) {
        var habitDto = habitService.createHabit(request,userId);
        var uri = uriBuilder.path("/habits/{id}").buildAndExpand(habitDto.getId()).toUri();
        return ResponseEntity.created(uri).body(habitDto);
    }

    @GetMapping("/users/{userId}/habits")
    public List<HabitResponse> getHabitsOfUser(@PathVariable Long userId) {
        return habitService.getHabitsOfUser(userId);
    }

    @GetMapping("/habits/{habitId}")
    public HabitResponse getHabit(@PathVariable Long habitId) {
        return habitService.getHabit(habitId);
    }

    @DeleteMapping("/habits/{habitId}")
    public ResponseEntity<Void> deleteHabit(@PathVariable Long habitId) {
        habitService.deleteHabit(habitId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/habits/{habitId}")
    public HabitResponse updateHabit(@PathVariable Long habitId,
                                     @RequestBody UpdateHabitRequest request) {
        return habitService.updateHabit(habitId, request);
    }

    @GetMapping("/habits/{habitId}/streak")
    public StreakResponse getStreak(@PathVariable Long habitId) {
        return habitService.getStreak(habitId);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> handleUserNotFound() {
        return ResponseEntity.notFound().build();
    }
}
