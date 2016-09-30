package com.aleksandrbogomolov.controller;

import com.aleksandrbogomolov.AbstractTest;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.when;

/**
 * Created by aleksandrbogomolov on 9/24/16.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "server.port=8888")
public class ContactControllerTest extends AbstractTest {

    private final String url = "http://localhost:8888/hello/contacts?nameFilter=";

    private final String contentType = "application/json;charset=UTF-8";

    @Test(expected = AssertionError.class)
    public void checkFilter() throws Exception {
        when().get(url + "^A.*$&lastId=0&limit=5")
              .then().statusCode(200).contentType(contentType)
              .body("name", Matchers.hasItems("Aleksandr"));
    }

    @Test
    public void getFilteredContacts() throws Exception {
        when().get(url + "^A.*$&lastId=0&limit=5")
              .then().statusCode(200).contentType(contentType)
              .body("name", Matchers.hasItems("Boris", "Fedor", "Grigoriy", "Petrov", "Ivanov"));
    }

    @Test
    public void getNextPageFilteredContacts() throws Exception {
        when().get(url + "^A.*$&lastId=15&limit=5")
              .then().statusCode(200).contentType(contentType)
              .body("name", Matchers.hasItems("Vasechkin", "Boris1", "Fedor1", "Grigoriy1", "Petrov1"));
    }

    @Test
    public void getFilteredContactsWithoutParameters() throws Exception {
        when().get(url + "^A.*$")
              .then().statusCode(200).contentType(contentType)
              .body("name", Matchers.hasItems("Boris", "Fedor", "Grigoriy", "Petrov", "Ivanov"));
    }

    @Test
    public void getAll() throws Exception {
        when().get(url + "^\\d.*$")
              .then().statusCode(200).contentType(contentType)
              .body("name", Matchers.hasItems("Aleksandr", "Boris", "Fedor", "Grigoriy", "Petrov"));
    }

    @Test
    public void noContent() throws Exception {
        when().get(url + "^.*$")
              .then().statusCode(204);
    }
}
