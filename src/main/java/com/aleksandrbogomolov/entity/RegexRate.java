package com.aleksandrbogomolov.entity;

/**
 * Created by aleksandrbogomolov on 10/2/16.
 */
public class RegexRate {

    private Integer id;

    private String regex;

    private int rate;

    public RegexRate() {
    }

    public RegexRate(String regex, int rate) {
        this.id = null;
        this.regex = regex;
        this.rate = rate;
    }

    public RegexRate(int id, String regex, int rate) {
        this.id = id;
        this.regex = regex;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "RegexRate{" +
               "id=" + id +
               ", regex='" + regex + '\'' +
               ", rate=" + rate +
               '}';
    }

    public boolean isNew() {
        return this.id == null;
    }
}
