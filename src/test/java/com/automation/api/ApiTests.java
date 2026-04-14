package com.automation.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ApiTests {

    // Базовый URL тестового API (JSONPlaceholder)
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    // Настройка базового URI перед выполнением тестов
    @BeforeClass
    public void setUp() {
        baseURI = BASE_URL;
    }

    // ========== GET ТЕСТЫ ==========

    // Получение списка всех постов
    @Test(groups = "positive", priority = 1)
    public void testGetPostsList() {
        given()
                .when()
                .get("/posts")
                .then()
                .statusCode(200)                     // проверка статус-кода
                .body("size()", greaterThan(0))      // список не пустой
                .body("[0].id", equalTo(1));         // первый пост имеет id=1
    }

    // Получение одного поста по id
    @Test(groups = "positive", priority = 2)
    public void testGetSinglePost() {
        given()
                .pathParam("postId", 1)              // передаем id в URL
                .when()
                .get("/posts/{postId}")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("userId", notNullValue())      // поле userId не пустое
                .body("title", notNullValue());      // поле title не пустое
    }

    // Получение постов по userId (фильтрация)
    @Test(groups = "positive", priority = 3)
    public void testGetPostsByUser() {
        given()
                .queryParam("userId", 1)             // параметр запроса
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].userId", equalTo(1));     // проверяем фильтрацию
    }

    // Негативный тест: пост не найден (404)
    @Test(groups = "negative", priority = 1)
    public void testGetPostNotFound() {
        given()
                .pathParam("postId", 999)            // несуществующий id
                .when()
                .get("/posts/{postId}")
                .then()
                .statusCode(404);                    // ожидаем "не найдено"
    }

    // ========== POST ТЕСТЫ ==========

    // Создание нового поста
    @Test(groups = "positive", priority = 4)
    public void testCreatePost() {
        String requestBody = "{ \"title\": \"Test Post\", \"body\": \"Content of test post\", \"userId\": 1 }";

        given()
                .contentType(ContentType.JSON)       // отправляем JSON
                .body(requestBody)                  // тело запроса
                .when()
                .post("/posts")
                .then()
                .statusCode(201)                     // создание
                .body("title", equalTo("Test Post"))
                .body("body", equalTo("Content of test post"))
                .body("userId", equalTo(1))
                .body("id", notNullValue());         // сервер сгенерировал id
    }

    // ========== PUT ТЕСТЫ ==========

    // Полное обновление поста
    @Test(groups = "positive", priority = 5)
    public void testUpdatePost() {
        String requestBody = "{ \"id\": 1, \"title\": \"Updated Title\", \"body\": \"Updated content\", \"userId\": 1 }";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .pathParam("postId", 1)
                .when()
                .put("/posts/{postId}")
                .then()
                .statusCode(200)
                .body("title", equalTo("Updated Title"))
                .body("body", equalTo("Updated content"));
    }

    // ========== PATCH ТЕСТЫ ==========

    // Частичное обновление поста
    @Test(groups = "positive", priority = 6)
    public void testPatchPost() {
        String requestBody = "{ \"title\": \"Patched Title\" }";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .pathParam("postId", 1)
                .when()
                .patch("/posts/{postId}")
                .then()
                .statusCode(200)
                .body("title", equalTo("Patched Title"));
    }

    // ========== DELETE ТЕСТЫ ==========

    // Удаление поста
    @Test(groups = "positive", priority = 7)
    public void testDeletePost() {
        given()
                .pathParam("postId", 1)
                .when()
                .delete("/posts/{postId}")
                .then()
                .statusCode(200);                    // успешное удаление
    }

    // ========== РАБОТА С ПОЛЬЗОВАТЕЛЯМИ ==========

    // Получение списка всех пользователей
    @Test(groups = "positive", priority = 8)
    public void testGetUsersList() {
        given()
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("size()", equalTo(10));        // 10 пользователей
    }

    // Получение одного пользователя
    @Test(groups = "positive", priority = 9)
    public void testGetSingleUser() {
        given()
                .pathParam("userId", 1)
                .when()
                .get("/users/{userId}")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", equalTo("Leanne Graham"))
                .body("email", equalTo("Sincere@april.biz"));
    }

    // Извлечение и проверка данных через JSON Path
    @Test(groups = "positive", priority = 10)
    public void testExtractAndVerifyResponseData() {
        Response response = given()
                .when()
                .get("/users/1");

        Assert.assertEquals(response.getStatusCode(), 200);

        String name = response.jsonPath().getString("name");
        Assert.assertEquals(name, "Leanne Graham");

        String email = response.jsonPath().getString("email");
        Assert.assertTrue(email.contains("@"));
    }

    // ========== ДОПОЛНИТЕЛЬНЫЕ ТЕСТЫ ==========

    // Получение задач (todos) для пользователя
    @Test(groups = "positive", priority = 11)
    public void testGetTodos() {
        given()
                .queryParam("userId", 1)
                .when()
                .get("/todos")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }
}