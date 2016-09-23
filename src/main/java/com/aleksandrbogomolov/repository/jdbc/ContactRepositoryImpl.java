package com.aleksandrbogomolov.repository.jdbc;

import com.aleksandrbogomolov.entity.Contact;
import com.aleksandrbogomolov.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by aleksandrbogomolov on 9/23/16.
 */
@Repository
@Transactional(readOnly = true)
public class ContactRepositoryImpl implements ContactRepository {

    private final JdbcTemplate template;

    private final RowMapper<Contact> mapper = new BeanPropertyRowMapper<>(Contact.class);

    @Autowired
    public ContactRepositoryImpl(@SuppressWarnings("SpringJavaAutowiringInspection") JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<Contact> getAll() {
        return template.query("SELECT * FROM contacts", mapper);
    }
}
