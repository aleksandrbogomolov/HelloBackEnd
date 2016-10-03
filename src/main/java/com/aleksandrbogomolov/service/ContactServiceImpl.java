package com.aleksandrbogomolov.service;

import com.aleksandrbogomolov.entity.Contact;
import com.aleksandrbogomolov.entity.RegexRate;
import com.aleksandrbogomolov.repository.ContactRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);

    private final ContactRepository repository;

    static Map<String, RegexRate> rates = new HashMap<>();

    @Autowired
    public ContactServiceImpl(ContactRepository repository) {
        this.repository = repository;
    }

    //После запуска приложения выбираем из БД таблицу с рейтами.
    @PostConstruct
    public void setRates() {
        log.info("Get rates from DB");
        repository.getRates().forEach(r -> rates.put(r.getRegex(), r));
    }

    //Перед остановкой приложения сохраняем рейты в БД.
    @PreDestroy
    @Override
    public void saveRates() {
        if (rates.size() > 0) {
            log.info("Save rates to DB");
            repository.saveRates(rates.values());
        }
    }

    @Override
    public List<Contact> getFilteredContacts(String regex, boolean forward, long lastId, int limit) {
        Pattern p = Pattern.compile(regex);
        //Создаем список для сбора отфильтрованных данных.
        List<Contact> contacts = new ArrayList<>();
        //Создаем счетчик для цикла.
        int count = 0;
        //Создаем переменную для управления рейтом.
        int rate;
        //Проверяем существует ли рейт по полученному регулярному выражению, если нет создаем новый RegexRate
        if (rates.get(regex) == null) {
            rates.put(regex, new RegexRate(regex, 0));
            rate = 0;
            log.info("Create new RegexRate with regex = {}", regex);
        } else rate = rates.get(regex).getRate();

        List<Contact> query;
        while (contacts.size() < limit && (query = getBackOrForwardFromRepo(forward, lastId, limit + rate)).size() != 0) {
            //Обрабатываем часть запроса и сохраняем во временной переменной.
            List<Contact> tempList = query.stream().filter(c -> !p.matcher(c.getName()).find()).collect(Collectors.toList());
            //Добавляем отфильтрованные данные к списку возвращаемых значений.
            contacts.addAll(tempList);

            //Проверяем были ли получены данные, если нет смещаем параметр lastId на величину limit.
            //иначе присваиваем lastId последний полученный из БД id.
            if (forward) {
                lastId = tempList.size() == 0 ? lastId + limit : contacts.get(contacts.size() - 1).getId();
            } else {
                lastId = tempList.size() == 0 ? lastId - limit : contacts.get(contacts.size() - 1).getId();
            }
            //Увеличиваем счетчики рейта и количества запросов.
            rate++; count++;
        }
        //После выхода из цикла проверяем количество запросов к БД и необходимость в сохранении рейта
        manipulateRate(regex, contacts.size(), limit, count, rate);

        return sortResult(contacts);
    }

    //Сортируем данные перед отправкой.
    private List<Contact> sortResult(List<Contact> contacts) {
        if (contacts.size() > 0) {
            return contacts.stream().sorted((o1, o2) -> ((int) (o1.getId() - o2.getId()))).collect(Collectors.toList());
        }
        else return contacts;
    }

    private List<Contact> getBackOrForwardFromRepo(boolean forward, long lastId, int limit) {
        if (forward) return repository.getForwardLimitAll(lastId, limit);
        else return repository.getBackLimitAll(lastId, limit);
    }

    private void manipulateRate(String regex, int size, int limit, int count, int rate) {
        RegexRate regexRate = rates.get(regex);
        if (size > limit && count == 1 && isExceeds(size, limit)) {
            regexRate.setRate(regexRate.getRate() - 1);
            log.info("Decrement rate for regex = {}", regex);
        } else if (count > 1) {
            regexRate.setRate(rate - 1);
            log.info("Increment rate for regex = {}", regex);
        }
    }

    //Проверяем превышает ли разница в полученых за один запрос данных больше limit на 20%
    private boolean isExceeds(int size, int limit) {
        return (size - limit) > ((limit * 20) / 100);
    }
}
