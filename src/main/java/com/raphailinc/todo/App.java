package com.raphailinc.todo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class App {

    public static void main(String[] args) {
        String dataDir = System.getenv().getOrDefault("TODO_DATA", "data");
        Path dbPath = Path.of(dataDir, "tasks.db");
        try {
            Files.createDirectories(dbPath.getParent());
        } catch (Exception e) {
            throw new RuntimeException("Cannot create data directory " + dataDir, e);
        }

        if (args.length == 0 || args[0].equals("help")) {
            printHelp();
            return;
        }

        String cmd = args[0];
        String[] params = slice(args);

        try (TaskRepository repo = new TaskRepository(dbPath.toString())) {
            switch (cmd) {
                case "add" -> handleAdd(repo, params);
                case "list" -> handleList(repo, params);
                case "done" -> handleDone(repo, params);
                case "delete" -> handleDelete(repo, params);
                case "clear-done" -> handleClearDone(repo);
                default -> {
                    System.out.println("Неизвестная команда: " + cmd);
                    printHelp();
                    System.exit(1);
                }
            }
        }
    }

    private static void handleAdd(TaskRepository repo, String[] params) {
        if (params.length < 1) {
            System.out.println("Использование: add <title> [--desc \"text\"] [--due 2024-12-31]");
            return;
        }
        String title = params[0];
        String desc = "";
        LocalDate due = null;
        for (int i = 1; i < params.length; i++) {
            if (params[i].equals("--desc") && i + 1 < params.length) {
                desc = params[++i];
            } else if (params[i].equals("--due") && i + 1 < params.length) {
                due = LocalDate.parse(params[++i]);
            }
        }
        Task task = repo.add(title, desc, due);
        System.out.println("Добавлена задача #" + task.id() + ": " + task.title());
    }

    private static void handleList(TaskRepository repo, String[] params) {
        boolean all = params.length > 0 && params[0].equals("--all");
        List<Task> tasks = repo.list(all);
        if (tasks.isEmpty()) {
            System.out.println("Список пуст.");
            return;
        }
        for (Task t : tasks) {
            String status = t.done() ? "[x]" : "[ ]";
            String due = t.dueDate() != null ? " (до " + t.dueDate() + ")" : "";
            System.out.printf("%s #%d %s%s%n", status, t.id(), t.title(), due);
            if (!t.description().isBlank()) {
                System.out.println("    " + t.description());
            }
        }
    }

    private static void handleDone(TaskRepository repo, String[] params) {
        if (params.length < 1) {
            System.out.println("Использование: done <id>");
            return;
        }
        int id = Integer.parseInt(params[0]);
        if (repo.markDone(id)) {
            System.out.println("Задача #" + id + " отмечена как выполненная.");
        } else {
            System.out.println("Задача #" + id + " не найдена.");
        }
    }

    private static void handleDelete(TaskRepository repo, String[] params) {
        if (params.length < 1) {
            System.out.println("Использование: delete <id>");
            return;
        }
        int id = Integer.parseInt(params[0]);
        if (repo.delete(id)) {
            System.out.println("Задача #" + id + " удалена.");
        } else {
            System.out.println("Задача #" + id + " не найдена.");
        }
    }

    private static void handleClearDone(TaskRepository repo) {
        int removed = repo.clearDone();
        System.out.println("Удалено выполненных задач: " + removed);
    }

    private static void printHelp() {
        System.out.println("""
                ToDo CLI команды:
                  add <title> [--desc "text"] [--due YYYY-MM-DD]  - добавить задачу
                  list [--all]                                     - показать задачи (по умолчанию только открытые)
                  done <id>                                        - отметить выполненной
                  delete <id>                                      - удалить задачу
                  clear-done                                       - удалить все выполненные
                  help                                             - показать помощь
                """);
    }

    private static String[] slice(String[] args) {
        if (args.length <= 1) return new String[0];
        String[] rest = new String[args.length - 1];
        System.arraycopy(args, 1, rest, 0, args.length - 1);
        return rest;
    }
}
