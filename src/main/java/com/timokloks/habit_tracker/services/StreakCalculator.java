package com.timokloks.habit_tracker.services;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiPredicate;

@Component
public class StreakCalculator {

    public <T> int getLongestStreak(List<T> completions, BiPredicate<T, T> isNext) {
        if(completions.isEmpty()) return 0;

        int longest = 1;
        int current = 1;

        for (int i = 1; i < completions.size(); i++) {
            T previous = completions.get(i - 1);
            T today = completions.get(i);

            if (isNext.test(previous, today)) {
                current++;
                longest = Math.max(longest, current);
            } else {
                current = 1;
            }
        }
        return longest;
    }
}
