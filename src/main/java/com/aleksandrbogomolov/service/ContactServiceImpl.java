package com.aleksandrbogomolov.service;

import com.aleksandrbogomolov.entity.Contact;
import com.aleksandrbogomolov.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
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
    public List<Contact> getFilteredContacts(String regex, int offset, int limit) {
        Pattern p = Pattern.compile(regex);
        return repository.getLimitAll(offset, limit).stream().filter(c -> !p.matcher(c.getName()).find()).collect(Collectors.toList());
    }
}
