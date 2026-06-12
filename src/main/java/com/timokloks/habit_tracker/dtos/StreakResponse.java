package com.timokloks.habit_tracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StreakResponse {
    private int currentStreak;
    private int longestStreak;
}
