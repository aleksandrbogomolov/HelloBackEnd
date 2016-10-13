package com.aleksandrbogomolov.repository;

import com.aleksandrbogomolov.entity.Contact;

import java.util.List;

/**
 * Created by aleksandrbogomolov on 9/23/16.
 */
public interface ContactRepository {

    List<Contact> getForwardLimitAll(long lastId, int limit);

    List<Contact> getBackLimitAll(long lastId, int limit);
}
