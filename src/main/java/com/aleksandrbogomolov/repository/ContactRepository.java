package com.aleksandrbogomolov.repository;

import com.aleksandrbogomolov.entity.Contact;

import java.util.List;

/**
 * Created by aleksandrbogomolov on 9/23/16.
 */
public interface ContactRepository {

    List<Contact> getFilteredContacts(String regex, boolean forward, long lastId, int limit);
}
