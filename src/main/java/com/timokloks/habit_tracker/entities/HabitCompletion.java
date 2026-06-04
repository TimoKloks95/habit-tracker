package com.timokloks.habit_tracker.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "habit_completions")
public class HabitCompletion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @ManyToOne
    @JoinColumn(name = "habit_id")
    private Habit habit;

    @PrePersist
    public void prePersist() {
        this.completedAt = LocalDateTime.now();
    }
}
