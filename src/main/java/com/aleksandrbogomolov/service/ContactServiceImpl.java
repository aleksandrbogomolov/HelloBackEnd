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
class ContactServiceImpl implements ContactService {

    private final ContactRepository repository;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public ContactServiceImpl(ContactRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Contact> getFilteredContacts(String regex, boolean forward, long lastId, int limit) {
        Pattern p = Pattern.compile(regex);
        List<Contact> contacts = new ArrayList<>();

        List<Contact> query;
        while (contacts.size() < limit && (query = getBackOrForwardFromRepo(forward, lastId, limit)).size() != 0) {
            List<Contact> tempList = query.stream().filter(c -> !p.matcher(c.getName()).find()).collect(Collectors.toList());
            contacts.addAll(tempList);
        }
        return prepareResult(contacts, limit);
    }

    private List<Contact> prepareResult(List<Contact> contacts, int limit) {
        if (contacts.size() > 0) {
            return contacts.subList(0, limit).stream().sorted((o1, o2) -> ((int) (o1.getId() - o2.getId()))).collect(Collectors.toList());
        } else return contacts;
    }

    private List<Contact> getBackOrForwardFromRepo(boolean forward, long lastId, int limit) {
        if (forward) return repository.getForwardLimitAll(lastId, limit);
        else return repository.getBackLimitAll(lastId, limit);
    }
}
