package com.aleksandrbogomolov.service;

import com.aleksandrbogomolov.entity.Contact;
import com.aleksandrbogomolov.entity.RegexRate;
import com.aleksandrbogomolov.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by aleksandrbogomolov on 9/23/16.
 */
@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository repository;

    private Map<String, RegexRate> rates = new HashMap<>();

    @Autowired
    public ContactServiceImpl(ContactRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void setRates() {
        repository.getRates().forEach(r -> rates.put(r.getRegex(), r));
    }

    @PreDestroy
    @Override
    public void saveRates() {
        repository.saveRates((List<RegexRate>) rates.values());
    }

    @Override
    public List<Contact> getFilteredContacts(String regex, long lastId, int limit) {
        Pattern p = Pattern.compile(regex);
        List<Contact> contacts = new ArrayList<>();
        List<Contact> query;
        int rate, count = 0;

        if (rates.get(regex) == null) {
            rates.put(regex, new RegexRate(regex, 0));
            rate = 0;
        } else rate = rates.get(regex).getRate();

        while (contacts.size() < limit && (query = repository.getLimitAll(lastId, limit + rate)).size() != 0) {
            contacts.addAll(query.stream().filter(c -> !p.matcher(c.getName()).find()).collect(Collectors.toList()));
            lastId = contacts.size() == 0 ? lastId + limit : contacts.get(contacts.size() - 1).getId();
            rate++; count++;
        }
        return contacts.size() > limit ? contacts.subList(0, limit) : contacts;
    }
}
