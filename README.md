# ToDo CLI

[![CI](https://github.com/Raphailinc/ToDo-CLI/actions/workflows/ci.yml/badge.svg)](https://github.com/Raphailinc/ToDo-CLI/actions/workflows/ci.yml)

[English](README.md) · [Русский](README.ru.md)

A command-line learning project for persistent task management in Java.

## What it does

- Add, list, complete and delete tasks.
- Store task data in SQLite.
- Configure the data directory through `TODO_DATA`.
- Test repository behavior with JUnit 5.

## Run locally

```bash
mvn clean package
java -jar target/todo-cli-1.0.0.jar help
```

## Checks

```bash
mvn test
```

## Scope and implementation notes

Requires JDK 17 and Maven. Data defaults to `./data/tasks.db`. This is a local single-user CLI, with no web interface or server.
