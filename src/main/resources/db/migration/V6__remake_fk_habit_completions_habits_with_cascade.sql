alter table habit_completions
drop
foreign key habit_completions_habits_id_fk;

alter table habit_completions
    add constraint habit_completions_habits_id_fk
        foreign key (habit_id) references habits (id)
            on delete cascade;