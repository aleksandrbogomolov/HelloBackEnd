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
        //Создаем список для сбора возвращаемых данных
        List<Contact> contacts = new ArrayList<>();
        //Создаем переменные для управления рейтом
        int rate, count = 0;

        //Проверяем существует ли рейт по полученному регулярному выражению, если нет создаем новый RegexRate
        if (rates.get(regex) == null) {
            rates.put(regex, new RegexRate(regex, 0));
            rate = 0;
        } else rate = rates.get(regex).getRate();

        List<Contact> query;
        while (contacts.size() < limit && (query = repository.getLimitAll(lastId, limit + rate)).size() != 0) {
            //Обрабатываем часть запроса и сохраняем во временной переменной
            List<Contact> tempList = query.stream().filter(c -> !p.matcher(c.getName()).find()).collect(Collectors.toList());
            //Добавляем обработанные данные к списку возвращаемых данных
            contacts.addAll(tempList);

            //Проверяем превышают ли количество строк полученных из БД необходимый лимит,
            //Если да и это был первый запрос к БД вызываем метод уменьшения рейта.
            if (tempList.size() > limit && count == 0) {
                decrementRate(regex, tempList.size(), limit);
            } else {
                //Если нет то проверяем были ли вообще получены данные,
                //если нет смещаем параметр lastId на величину limit.
                //иначе присваиваем lastId последний полученный из БД id.
                lastId = contacts.size() == 0 ? lastId + limit : contacts.get(contacts.size() - 1).getId();
                //Увеличиваем счетчики рейта и количества запросов.
                rate++; count++;
            }
        }
        //После выхода из цикла проверяем количество запросов к БД, если больше 1, то увеличиваем рейт
        //на величину равную количеству запросов.
        if (count > 1) {
            rates.get(regex).setRate(rate - 1);
        }

        //Возвращаем отфльтрованные данные в количестве равном limit.
        return contacts.size() > limit ? contacts.subList(0, limit) : contacts;
    }

    //Проверяем если разница в полученых за один запрос данных больше limit на 20%, то уменьшаем рейт
    private void decrementRate(String regex, int size, int limit) {
        if ((size - limit) > ((limit * 20) / 100)) {
            RegexRate rate = rates.get(regex);
            rate.setRate(rate.getRate() - 1);
        }
    }
}
