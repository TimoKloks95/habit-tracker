package com.timokloks.habit_tracker.repositories;

import com.timokloks.habit_tracker.dtos.HabitResponse;
import com.timokloks.habit_tracker.entities.Habit;
import com.timokloks.habit_tracker.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitRepository extends JpaRepository<Habit, Long> {
    List<Habit> findByUser(User user);
}