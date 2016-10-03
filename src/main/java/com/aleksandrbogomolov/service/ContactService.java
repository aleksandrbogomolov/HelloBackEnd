package com.aleksandrbogomolov.service;

import com.aleksandrbogomolov.entity.Contact;
import com.aleksandrbogomolov.entity.RegexRate;

import java.util.List;

/**
 * Created by aleksandrbogomolov on 9/23/16.
 */
public interface ContactService {

    List<Contact> getFilteredContacts(String regex, boolean forward, long lastId, int limit);

    void saveRates();
}
