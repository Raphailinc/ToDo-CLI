package com.raphailinc.todo;

import java.time.LocalDate;
import java.util.Objects;

public final class Task {
    private final int id;
    private final String title;
    private final String description;
    private final LocalDate dueDate;
    private final boolean done;

    public Task(int id, String title, String description, LocalDate dueDate, boolean done) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.done = done;
    }

    public int id() {
        return id;
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }

    public LocalDate dueDate() {
        return dueDate;
    }

    public boolean done() {
        return done;
    }

    public Task markDone() {
        return new Task(id, title, description, dueDate, true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return id == task.id &&
                done == task.done &&
                Objects.equals(title, task.title) &&
                Objects.equals(description, task.description) &&
                Objects.equals(dueDate, task.dueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, dueDate, done);
    }
}
