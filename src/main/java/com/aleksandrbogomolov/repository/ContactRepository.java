package com.aleksandrbogomolov.repository;

import com.aleksandrbogomolov.entity.Contact;
import com.aleksandrbogomolov.entity.RegexRate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by aleksandrbogomolov on 9/23/16.
 */
public interface ContactRepository {

    List<Contact> getLimitAll(long lastId, int limit);

    List<RegexRate> getRates();

    void saveRates(Collection<RegexRate> rates);
}
