package com.aleksandrbogomolov.service;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.when;

/**
 * Created by aleksandrbogomolov on 9/23/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ContactServiceImplTest {

    @Test
    public void getFilteredContacts() throws Exception {
        when().get("http://localhost:8080/hello/contacts?nameFilter=^A.*$&offset=0&limit=5")
              .then().statusCode(200)
              .body("name", Matchers.hasItems("Boris", "Fedor", "Grigoriy", "Petrov"));
    }
}
