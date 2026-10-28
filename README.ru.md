# ToDo CLI

Минималистичный менеджер задач на Java 17 с хранением в SQLite. Без UI, зато быстрый CLI, конфиг через переменные окружения и автотесты.

## Что сделано
- Полный рефактор: убран старый NetBeans/GUI код и бинарные артефакты, добавлен Maven-проект с чистой структурой `src/main/java`.
- Хранилище задач в SQLite (`TaskRepository`) с автоинициализацией схемы.
- CLI (`App`) с командами: `add`, `list`, `done`, `delete`, `clear-done`, `help`.
- Тесты на JUnit 5 (`src/test/java`), Maven Surefire настроен.

## Установка и запуск
```bash
mvn clean package
java -jar target/todo-cli-1.0.0.jar help
```

По умолчанию данные лежат в `./data/tasks.db`. Можно задать:
```bash
export TODO_DATA=/path/to/store
java -jar target/todo-cli-1.0.0.jar add "Протестировать CLI" --desc "Сделать скрин" --due 2024-12-31
java -jar target/todo-cli-1.0.0.jar list
java -jar target/todo-cli-1.0.0.jar done 1
java -jar target/todo-cli-1.0.0.jar clear-done
```

## Команды
- `add <title> [--desc "text"] [--due YYYY-MM-DD]`
- `list [--all]` — без `--all` показывает только невыполненные.
- `done <id>` — отметить выполненной.
- `delete <id>` — удалить задачу.
- `clear-done` — удалить все выполненные задачи.
- `help` — справка.

## Тесты
```bash
mvn test
```

## Примечания
- SQLite файл создаётся автоматически; директория данных задаётся переменной `TODO_DATA`.
- Код использует Java 17, зависимостей минимум: `sqlite-jdbc` и `junit-jupiter` (test scope).
