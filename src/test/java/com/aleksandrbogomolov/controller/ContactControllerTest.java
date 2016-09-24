package com.aleksandrbogomolov.controller;

import com.aleksandrbogomolov.AbstractTest;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.when;
import static org.junit.Assert.*;

/**
 * Created by aleksandrbogomolov on 9/24/16.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "server.port=8888")
public class ContactControllerTest extends AbstractTest {

    private final String url = "/hello/contacts?nameFilter=";

    private final String contentType = "application/json;charset=UTF-8";

    @Test(expected = AssertionError.class)
    public void checkFilter() throws Exception {
        when().get(url + "^A.*$&offset=0&limit=5")
              .then().statusCode(200).contentType(contentType)
              .body("name", Matchers.hasItems("Aleksandr"));
    }

    @Test
    public void getFilteredContacts() throws Exception {
        when().get(url + "^A.*$&offset=0&limit=5")
              .then().statusCode(200).contentType(contentType)
              .body("name", Matchers.hasItems("Boris", "Fedor", "Grigoriy", "Petrov"));
    }

    @Test
    public void getFilteredContactsWithoutParameters() throws Exception {
        when().get(url + "^A.*$")
              .then().statusCode(200).contentType(contentType)
              .body("name", Matchers.hasItems("Boris", "Fedor", "Grigoriy", "Petrov"));
    }

    @Test
    public void getAll() throws Exception {
        when().get(url + "^\\d.*$")
              .then().statusCode(200).contentType(contentType)
              .body("name", Matchers.hasItems("Aleksandr", "Boris", "Fedor", "Grigoriy", "Petrov"));
    }
}
