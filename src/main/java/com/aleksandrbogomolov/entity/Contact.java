package com.aleksandrbogomolov.entity;

/**
 * Created by aleksandrbogomolov on 9/23/16.
 */
public class Contact {

    private long id;

    private String name;

    public Contact() {
    }

    public Contact(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
