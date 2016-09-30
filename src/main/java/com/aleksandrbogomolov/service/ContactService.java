package com.aleksandrbogomolov.service;

import com.aleksandrbogomolov.entity.Contact;

import java.util.List;

/**
 * Created by aleksandrbogomolov on 9/23/16.
 */
public interface ContactService {

    List<Contact> getFilteredContacts(String regex, long lastId, int limit);
}
