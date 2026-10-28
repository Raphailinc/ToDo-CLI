# ToDo CLI

[![CI](https://github.com/Raphailinc/ToDo-CLI/actions/workflows/ci.yml/badge.svg)](https://github.com/Raphailinc/ToDo-CLI/actions/workflows/ci.yml)

[English](README.md) · [Русский](README.ru.md)

Учебный консольный менеджер задач на Java с постоянным хранением данных.

## Возможности

- Создание, просмотр, завершение и удаление задач.
- Хранение в SQLite.
- Настройка каталога данных через TODO_DATA.
- Проверка репозитория через JUnit 5.

## Локальный запуск

```bash
mvn clean package
java -jar target/todo-cli-1.0.0.jar help
```

## Проверки

```bash
mvn test
```

## Условия запуска и границы проекта

Нужны JDK 17 и Maven. По умолчанию данные находятся в ./data/tasks.db. Это локальная однопользовательская CLI-программа без веб-интерфейса и сервера. Maven собирает исполняемый JAR с зависимостями.
