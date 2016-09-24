package com.aleksandrbogomolov.controller;

import com.aleksandrbogomolov.entity.Contact;
import com.aleksandrbogomolov.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final ContactService service;

    @Autowired
    public ContactController(ContactService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Contact> getFilteredContact(@RequestParam(name = "nameFilter") String regex,
                                            @RequestParam(name = "offset", required = false) Integer offset,
                                            @RequestParam(name = "limit", required = false) Integer limit) {
        return service.getFilteredContacts(regex, offset == null? 0 : offset, limit == null || limit > 50 ? 5: limit);
    }
}
