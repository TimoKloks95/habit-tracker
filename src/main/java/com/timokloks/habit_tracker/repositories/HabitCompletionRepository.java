package com.timokloks.habit_tracker.repositories;

import com.timokloks.habit_tracker.entities.HabitCompletion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitCompletionRepository extends JpaRepository<HabitCompletion, Long> {
}