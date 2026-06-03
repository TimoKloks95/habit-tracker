package com.timokloks.habit_tracker.repositories;

import com.timokloks.habit_tracker.entities.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitRepository extends JpaRepository<Habit, Long> {
}