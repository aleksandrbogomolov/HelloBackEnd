package com.aleksandrbogomolov.controller;

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
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "server.port=8888")
public class ContactControllerTest {

    @Test
    public void getFilteredContacts() throws Exception {
        when().get("http://localhost:8888/hello/contacts?nameFilter=^A.*$&offset=0&limit=5")
              .then().statusCode(200).contentType("application/json;charset=UTF-8")
              .body("name", Matchers.hasItems("Boris", "Fedor", "Grigoriy", "Petrov"));
    }

    @Test
    public void getFilteredContactsWithoutParameters() throws Exception {
        when().get("http://localhost:8888/hello/contacts?nameFilter=^A.*$")
              .then().statusCode(200).contentType("application/json;charset=UTF-8")
              .body("name", Matchers.hasItems("Boris", "Fedor", "Grigoriy", "Petrov"));
    }
}
