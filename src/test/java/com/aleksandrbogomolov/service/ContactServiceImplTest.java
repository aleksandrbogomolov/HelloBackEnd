package com.aleksandrbogomolov.service;

import com.aleksandrbogomolov.AbstractTest;
import com.aleksandrbogomolov.entity.RegexRate;
import com.aleksandrbogomolov.repository.ContactRepository;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

/**
 * Created by aleksandrbogomolov on 10/2/16.
 */
@SpringBootTest
@Transactional
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
public class ContactServiceImplTest extends AbstractTest {

    private final RegexRate testData1 = new RegexRate("^A.*$", 0);

    private final RegexRate testData2 = new RegexRate("^.*[ai].*$", 0);

    private final RegexRate testData3 = new RegexRate("^.*$", 0);

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private ContactRepository repository;

    @Test
    public void getRatesFromDb() throws Exception {
        ImmutableList<RegexRate> testList = ImmutableList.of(testData1, testData2);
        assertTrue(testList.equals(repository.getRates()));
    }

    @Test
    public void saveRates() throws Exception {
        RegexRate testSaveData = ContactServiceImpl.rates.get(testData1.getRegex());
        testSaveData.setRate(1);
        ImmutableList<RegexRate> testList = ImmutableList.of(testSaveData, ContactServiceImpl.rates.get(testData2.getRegex()), testData3);
        repository.saveRates(testList);
        assertTrue(testList.equals(repository.getRates().stream().sorted((o1, o2) -> o1.getId() - o2.getId()).collect(Collectors.toList())));
    }
}
