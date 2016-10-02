package com.aleksandrbogomolov.repository.jdbc;

import com.aleksandrbogomolov.entity.Contact;
import com.aleksandrbogomolov.entity.RegexRate;
import com.aleksandrbogomolov.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aleksandrbogomolov on 9/23/16.
 */
@Repository
@Transactional(readOnly = true)
public class ContactRepositoryImpl implements ContactRepository {

    private final JdbcTemplate template;

    private final RowMapper<Contact> contactMapper = new BeanPropertyRowMapper<>(Contact.class);

    private final RowMapper<RegexRate> rateMapper = new BeanPropertyRowMapper<>(RegexRate.class);

    @Autowired
    public ContactRepositoryImpl(@SuppressWarnings("SpringJavaAutowiringInspection") JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<Contact> getLimitAll(long lastId, int limit) {
        return template.query("SELECT * FROM contacts WHERE id > ? ORDER BY id LIMIT ?", contactMapper, lastId, limit);
    }

    @Override
    public List<RegexRate> getRates() {
        return template.query("SELECT * FROM rates", rateMapper);
    }

    @Override
    public void saveRates(List<RegexRate> rates) {

    }
}
