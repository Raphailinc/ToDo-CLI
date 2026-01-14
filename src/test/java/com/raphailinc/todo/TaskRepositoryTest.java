package com.raphailinc.todo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskRepositoryTest {
    private Path tempDb;

    private TaskRepository repo() {
        try {
            tempDb = Files.createTempFile("todo-test", ".db");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new TaskRepository(tempDb.toString());
    }

    @AfterEach
    void cleanup() throws IOException {
        if (tempDb != null) {
            Files.deleteIfExists(tempDb);
        }
    }

    @Test
    void addAndListTasks() {
        try (TaskRepository r = repo()) {
            Task t1 = r.add("Task1", "Desc", LocalDate.parse("2024-12-31"));
            Task t2 = r.add("Task2", "", null);

            List<Task> open = r.list(false);
            assertEquals(2, open.size());
            assertEquals(t1.title(), open.get(0).title());

            assertTrue(r.markDone(t1.id()));
            List<Task> pending = r.list(false);
            assertEquals(1, pending.size());
            assertEquals(t2.id(), pending.get(0).id());
        }
    }

    @Test
    void deleteAndClear() {
        try (TaskRepository r = repo()) {
            Task t1 = r.add("A", "", null);
            Task t2 = r.add("B", "", null);
            assertTrue(r.delete(t1.id()));
            assertFalse(r.delete(999));
            assertTrue(r.markDone(t2.id()));
            int removed = r.clearDone();
            assertEquals(1, removed);
            assertTrue(r.list(true).isEmpty());
        }
    }
}
