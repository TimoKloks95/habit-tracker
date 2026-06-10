package com.timokloks.habit_tracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeekKey {
    private int year;
    private int week;
}
