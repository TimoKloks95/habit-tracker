package com.timokloks.habit_tracker.repositories;

import com.timokloks.habit_tracker.entities.Habit;
import com.timokloks.habit_tracker.entities.HabitCompletion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HabitCompletionRepository extends JpaRepository<HabitCompletion, Long> {
    List<HabitCompletion> findByHabit(Habit habit);
    Optional<HabitCompletion> findByHabitAndId(Habit habit, Long id);
}