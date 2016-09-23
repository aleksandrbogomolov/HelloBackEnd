package com.aleksandrbogomolov.entity;

import javax.persistence.*;

/**
 * Created by aleksandrbogomolov on 9/23/16.
 */
@Entity
@Table(name = "contacts")
public class Contact {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "name")
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
