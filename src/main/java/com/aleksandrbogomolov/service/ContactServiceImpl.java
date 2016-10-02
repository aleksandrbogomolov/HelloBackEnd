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

    static Map<String, RegexRate> rates = new HashMap<>();

    @Autowired
    public ContactServiceImpl(ContactRepository repository) {
        this.repository = repository;
    }

    //После запуска приложения выбираем из БД таблицу с рейтами
    @PostConstruct
    public void setRates() {
        repository.getRates().forEach(r -> rates.put(r.getRegex(), r));
    }

    //Перед остановкой приложения сохраняем рейты в БД
    @PreDestroy
    @Override
    public void saveRates() {
        if (rates.size() > 0) {
            repository.saveRates(rates.values());
        }
    }

    @Override
    public List<Contact> getFilteredContacts(String regex, long lastId, int limit) {
        Pattern p = Pattern.compile(regex);
        List<Contact> contacts = new ArrayList<>();
        int rate, count = 0;

        if (rates.get(regex) == null) {
            rates.put(regex, new RegexRate(regex, 0));
            rate = 0;
        } else rate = rates.get(regex).getRate();

        List<Contact> query;
        while (contacts.size() < limit && (query = repository.getLimitAll(lastId, limit + rate)).size() != 0) {
            List<Contact> tempList = query.stream().filter(c -> !p.matcher(c.getName()).find()).collect(Collectors.toList());
            contacts.addAll(tempList);
            lastId = contacts.size() == 0 ? lastId + limit : contacts.get(contacts.size() - 1).getId();

            if (tempList.size() > limit && count == 0) {
                incrementRate(regex, tempList.size(), limit);
            } else {
                rate++; count++;
            }
        }
        if (count > 1) {
            rates.get(regex).setRate(rate - 1);
        }

        return contacts.size() > limit ? contacts.subList(0, limit) : contacts;
    }

    private void incrementRate(String regex, int size, int limit) {
        if ((size - limit) > ((limit * 20) / 100)) {
            RegexRate rate = rates.get(regex);
            rate.setRate(rate.getRate() - 1);
        }
    }
}
