```markdown
# API Tests with RestAssured

Автотесты для REST API с использованием RestAssured и TestNG.

## Технологии
- Java
- RestAssured
- TestNG
- Maven

## Что тестируется
- GET /posts — получение списка постов
- GET /posts/{id} — получение одного поста
- GET /posts?userId={id} — фильтрация по пользователю
- POST /posts — создание нового поста
- PUT /posts/{id} — полное обновление поста
- PATCH /posts/{id} — частичное обновление поста
- DELETE /posts/{id} — удаление поста
- GET /users — получение списка пользователей
- GET /users/{id} — получение одного пользователя
- GET /todos — получение задач

## Тестовые данные
Используется бесплатный тестовый API: [JSONPlaceholder](https://jsonplaceholder.typicode.com)

## Как запустить

### Все тесты
```bash
mvn clean test
```

### Только позитивные тесты
```bash
mvn test -Dgroups=positive
```

### Только негативные тесты
```bash
mvn test -Dgroups=negative
```

## Структура проекта
```
apiTestsRestassured/
├── pom.xml
├── src/test/java/com/automation/api/
│   └── ApiTests.java
└── README.md
```
