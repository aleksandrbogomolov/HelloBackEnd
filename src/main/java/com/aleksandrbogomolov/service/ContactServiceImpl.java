package com.aleksandrbogomolov.service;

import com.aleksandrbogomolov.entity.Contact;
import com.aleksandrbogomolov.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by aleksandrbogomolov on 9/23/16.
 */
@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository repository;

    @Autowired
    public ContactServiceImpl(ContactRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Contact> getFilteredContacts(String regex, long lastId, int limit) {
        Pattern p = Pattern.compile(regex);
        List<Contact> contacts = new ArrayList<>();
        List<Contact> query;
        while ((query = repository.getLimitAll(lastId, limit)).size() != 0) {
            contacts.addAll(query.stream().filter(c -> !p.matcher(c.getName()).find()).collect(Collectors.toList()));
            lastId = contacts.size() == 0 ? lastId + limit : contacts.get(contacts.size() - 1).getId();
        }
        return contacts.size() > limit ? contacts.subList(0, limit) : contacts;
    }
}
