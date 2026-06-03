package com.timokloks.habit_tracker.controllers;

import com.timokloks.habit_tracker.dtos.CreateHabitRequest;
import com.timokloks.habit_tracker.dtos.HabitResponse;
import com.timokloks.habit_tracker.mappers.HabitMapper;
import com.timokloks.habit_tracker.repositories.HabitRepository;
import com.timokloks.habit_tracker.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
@RequestMapping
public class HabitController
{
    private final UserRepository userRepository;
    private final HabitMapper habitMapper;
    private final HabitRepository habitRepository;

    @PostMapping("users/{id}/habits")
    public ResponseEntity<HabitResponse> createHabit(@Valid @RequestBody CreateHabitRequest request,
                                                     @PathVariable Long id,
                                                     UriComponentsBuilder uriBuilder) {
        var user = userRepository.findById(id).orElse(null);
        if(user == null) {
            return ResponseEntity.notFound().build();
        }

        var habit = habitMapper.toEntity(request);
        habit.setUser(user);

        habitRepository.save(habit);

        var habitDto = habitMapper.toDto(habit);

        var uri = uriBuilder.path("/habits/{id}").buildAndExpand(habitDto.getId()).toUri();
        return ResponseEntity.created(uri).body(habitDto);
    }
}
