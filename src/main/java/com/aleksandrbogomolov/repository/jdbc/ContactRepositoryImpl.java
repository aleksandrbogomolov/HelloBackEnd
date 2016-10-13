package com.aleksandrbogomolov.repository.jdbc;

import com.aleksandrbogomolov.entity.Contact;
import com.aleksandrbogomolov.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

/**
 * Created by aleksandrbogomolov on 9/23/16.
 */
@Repository
@Transactional(readOnly = true)
public class ContactRepositoryImpl implements ContactRepository {

    private final DataSource dataSource;

    private final RowMapper<Contact> contactMapper = new BeanPropertyRowMapper<>(Contact.class);

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public ContactRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Contact> getForwardLimitAll(long lastId, int limit) {
        try {
            Connection connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            CallableStatement statement = connection.prepareCall("{? = call get_forward(?)}");
            statement.registerOutParameter(1, Types.OTHER);
            statement.setLong(2, lastId);
            statement.execute();
            ResultSet result = (ResultSet) statement.getObject(1);
            while (result.next()) {
                System.out.println(result.getString(2));
            }
            result.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Contact> getBackLimitAll(long lastId, int limit) {
        return null;
    }
}
