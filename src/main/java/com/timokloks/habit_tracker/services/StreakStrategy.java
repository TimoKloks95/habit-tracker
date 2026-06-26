package com.timokloks.habit_tracker.services;

import com.timokloks.habit_tracker.entities.HabitCompletion;

import java.util.List;

public interface StreakStrategy {
    int calculateCurrentStreak(List<HabitCompletion> completions);
    int calculateLongestStreak(List<HabitCompletion> completions);
}
