package com.aleksandrbogomolov.repository.jdbc;

import com.aleksandrbogomolov.entity.Contact;
import com.aleksandrbogomolov.entity.RegexRate;
import com.aleksandrbogomolov.repository.ContactRepository;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by aleksandrbogomolov on 9/23/16.
 */
@Repository
@Transactional(readOnly = true)
public class ContactRepositoryImpl implements ContactRepository {

    private final Logger log = LoggerFactory.getLogger(ContactRepositoryImpl.class);

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

    @Transactional
    @Override
    public void saveRates(Collection<RegexRate> rates) {
        //Разделяем данные рейтов на новые и старые и сохраняем в БД
        //-Новые
        ImmutableList<RegexRate> newRates = ImmutableList.copyOf(rates.stream().filter(RegexRate::isNew).collect(Collectors.toList()));
        Iterator<RegexRate> iteratorIsNew = newRates.iterator();
        template.batchUpdate("INSERT INTO rates (regex, rate) VALUES (?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                RegexRate rate = iteratorIsNew.next();
                ps.setString(1, rate.getRegex());
                ps.setInt(2, rate.getRate());
            }

            @Override
            public int getBatchSize() {
                log.info("Save {} new RegexRate to DB", newRates.size());
                return newRates.size();
            }
        });
        //-Старые
        ImmutableList<RegexRate> oldRates = ImmutableList.copyOf(rates.stream().filter(r -> !r.isNew()).collect(Collectors.toList()));
        Iterator<RegexRate> iterator = oldRates.iterator();
        template.batchUpdate("UPDATE rates SET regex = ?, rate = ? WHERE id = ?", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                RegexRate rate = iterator.next();
                ps.setString(1, rate.getRegex());
                ps.setInt(2, rate.getRate());
                ps.setInt(3, rate.getId());
            }

            @Override
            public int getBatchSize() {
                log.info("Update {} RegexRate into DB", oldRates.size());
                return oldRates.size();
            }
        });
    }
}
