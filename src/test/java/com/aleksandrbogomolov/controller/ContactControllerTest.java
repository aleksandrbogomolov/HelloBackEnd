package com.aleksandrbogomolov.controller;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.when;

/**
 * Created by aleksandrbogomolov on 9/24/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "server.port=8888")
public class ContactControllerTest {

    private final String url = "http://localhost:8888/hello/contacts?nameFilter=";

    private final String contentType = "application/json;charset=UTF-8";

    @Test
    public void getFilteredContacts() throws Exception {
        when().get(url + "^A.*$&forward=true&lastId=0&limit=5")
              .then().statusCode(200).contentType(contentType)
              .body("name", Matchers.hasItems("Boris", "Fedor", "Grigoriy", "Petrov", "Ivanov"));
    }

    @Test
    public void getNextPageFilteredContacts() throws Exception {
        when().get(url + "^A.*$&forward=true&lastId=6&limit=5")
              .then().statusCode(200).contentType(contentType)
              .body("name", Matchers.hasItems("Vasechkin", "Boris1", "Fedor1", "Grigoriy1", "Petrov1"));
    }

    @Test
    public void getPreviousPageFilteredContacts() throws Exception {
        when().get(url + "^A.*$&forward=false&lastId=21&limit=5")
              .then().statusCode(200).contentType(contentType)
              .body("name", Matchers.hasItems("Grigoriy", "Petrov", "Ivanov", "Vasechkin", "Boris1"));
    }

    @Test
    public void getFilteredContactsWithoutParameters() throws Exception {
        when().get(url + "^A.*$&forward=true")
              .then().statusCode(200).contentType(contentType)
              .body("name", Matchers.hasItems("Boris", "Fedor", "Grigoriy", "Petrov", "Ivanov"));
    }

    @Test
    public void getAll() throws Exception {
        when().get(url + "^\\d.*$&forward=true")
              .then().statusCode(200).contentType(contentType)
              .body("name", Matchers.hasItems("Aleksandr", "Boris", "Fedor", "Grigoriy", "Petrov"));
    }

    @Test
    public void noContent() throws Exception {
        when().get(url + "^.*$&forward=true")
              .then().statusCode(404);
    }
}
