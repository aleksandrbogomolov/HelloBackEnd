package com.aleksandrbogomolov.entity;

import com.google.common.base.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegexRate)) return false;
        RegexRate rate1 = (RegexRate) o;
        return rate == rate1.rate &&
               Objects.equal(regex, rate1.regex);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(regex, rate);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                      .add("id", id)
                      .add("regex", regex)
                      .add("rate", rate)
                      .toString();
    }

    public boolean isNew() {
        return this.id == null;
    }
}
