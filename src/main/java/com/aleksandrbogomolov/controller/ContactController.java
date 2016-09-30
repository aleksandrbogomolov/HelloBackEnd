package com.aleksandrbogomolov.controller;

import com.aleksandrbogomolov.entity.Contact;
import com.aleksandrbogomolov.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by aleksandrbogomolov on 9/23/16.
 */
@RestController
@RequestMapping(value = "/hello/contacts")
public class ContactController {

    private final Logger log = LoggerFactory.getLogger(ContactController.class);

    private final ContactService service;

    @Autowired
    public ContactController(ContactService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Contact>> getFilteredContact(
            @RequestParam(name = "nameFilter") String regex,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit) {

        log.info("Get filtered contacts");

        int offsetParam = offset == null ? 0 : offset;
        int limitParam = limit == null || limit > 5 ? 5 : limit;

        List<Contact> contacts = service.getFilteredContacts(regex, offsetParam, limitParam);

        if (contacts.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }
}
