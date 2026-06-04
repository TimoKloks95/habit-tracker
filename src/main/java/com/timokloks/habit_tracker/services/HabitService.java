package com.timokloks.habit_tracker.services;

import com.timokloks.habit_tracker.dtos.CreateHabitRequest;
import com.timokloks.habit_tracker.dtos.HabitResponse;
import com.timokloks.habit_tracker.exceptions.HabitNotFoundException;
import com.timokloks.habit_tracker.exceptions.UserNotFoundException;
import com.timokloks.habit_tracker.mappers.HabitMapper;
import com.timokloks.habit_tracker.repositories.HabitRepository;
import com.timokloks.habit_tracker.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class HabitService {

    private final UserRepository userRepository;
    private final HabitMapper habitMapper;
    private final HabitRepository habitRepository;

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
}
