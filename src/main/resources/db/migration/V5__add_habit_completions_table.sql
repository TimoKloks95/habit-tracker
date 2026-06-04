create table habit_completions
(
    id           BIGINT auto_increment
        primary key,
    habit_id     BIGINT not null,
    completed_at TIMESTAMP default CURRENT_TIMESTAMP null,
    constraint habit_completions_habits_id_fk
        foreign key (habit_id) references habits (id)
);
