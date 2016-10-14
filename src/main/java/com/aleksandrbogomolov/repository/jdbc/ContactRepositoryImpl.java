package com.aleksandrbogomolov.repository.jdbc;

import com.aleksandrbogomolov.entity.Contact;
import com.aleksandrbogomolov.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by aleksandrbogomolov on 9/23/16.
 */
@Repository
@Transactional(readOnly = true)
public class ContactRepositoryImpl implements ContactRepository {

    private final DataSource dataSource;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public ContactRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Contact> getFilteredContacts(String regex, boolean forward, long lastId, int limit) {
        Pattern p = Pattern.compile(regex);
        List<Contact> contacts = new ArrayList<>();
        Connection connection = null;
        CallableStatement statement;
        ResultSet result = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            if (forward) {
                statement = connection.prepareCall("{? = call get_forward(?)}");
            } else statement = connection.prepareCall("{? = call get_back(?)}");
            statement.registerOutParameter(1, Types.OTHER);
            statement.setLong(2, lastId);
            statement.execute();
            result = (ResultSet) statement.getObject(1);
            while (contacts.size() < limit && result.next()) {
                if (!p.matcher(result.getString(2)).find())
                    contacts.add(new Contact(result.getLong(1), result.getString(2)));
            }
            result.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) result.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return contacts;
    }
}
