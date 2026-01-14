package com.raphailinc.todo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository implements AutoCloseable {
    private final Connection connection;

    public TaskRepository(String dbPath) {
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            initSchema();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to open database", e);
        }
    }

    private void initSchema() throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS tasks (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    description TEXT DEFAULT '',
                    due_date TEXT,
                    done INTEGER DEFAULT 0
                )
            """);
        }
    }

    public Task add(String title, String description, LocalDate dueDate) {
        final String sql = "INSERT INTO tasks(title, description, due_date, done) VALUES(?, ?, ?, 0)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, title);
            ps.setString(2, description);
            if (dueDate != null) {
                ps.setString(3, dueDate.toString());
            } else {
                ps.setNull(3, Types.VARCHAR);
            }
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Task(rs.getInt(1), title, description, dueDate, false);
                }
            }
            throw new SQLException("No generated key returned");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert task", e);
        }
    }

    public List<Task> list(boolean includeDone) {
        List<Task> tasks = new ArrayList<>();
        String sql = includeDone ? "SELECT * FROM tasks ORDER BY done, due_date NULLS LAST, id" :
                "SELECT * FROM tasks WHERE done = 0 ORDER BY due_date NULLS LAST, id";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tasks.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to query tasks", e);
        }
        return tasks;
    }

    public boolean markDone(int id) {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE tasks SET done = 1 WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to mark task done", e);
        }
    }

    public boolean delete(int id) {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM tasks WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete task", e);
        }
    }

    public int clearDone() {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM tasks WHERE done = 1")) {
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clear done tasks", e);
        }
    }

    private Task map(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        String description = rs.getString("description");
        String due = rs.getString("due_date");
        boolean done = rs.getInt("done") == 1;
        LocalDate dueDate = due != null ? LocalDate.parse(due) : null;
        return new Task(id, title, description, dueDate, done);
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException ignored) {
        }
    }
}
