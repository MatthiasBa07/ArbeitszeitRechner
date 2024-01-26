package org.example.zeitrechner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TimestampJDBCDaoTest {

    @Test
    @Order(1)
    void insertTimestamp() throws SQLException {
        PersonJDBCDao.getInstance().insertPerson("Test","Person");
        TimestampJDBCDao.getInstance().insertTimestamp(PersonJDBCDao.getInstance().getPersonByName("Test","Person"),LocalDate.of(2024,1,1),1 );
        assertNotNull(TimestampJDBCDao.getInstance().getTimestampByPerson(PersonJDBCDao.getInstance().getPersonByName("Test","Person")));
    }

    @Test
    @Order(2)
    void getTimestampById() throws SQLException {
        assertNotNull(TimestampJDBCDao.getInstance().getTimestampById(TimestampJDBCDao.getInstance().getTimestampByPerson(PersonJDBCDao.getInstance().getPersonByName("Test","Person")).getFirst().getId()));
    }

    @Test
    @Order(3)
    void getTimestampByTimestamp() throws SQLException {
        System.out.println(TimestampJDBCDao.getInstance().getTimestampByPerson(PersonJDBCDao.getInstance().getPersonByName("Test","Person")).getFirst());
        assertNotNull(TimestampJDBCDao.getInstance().getTimestampByTimestamp(TimestampJDBCDao.getInstance().getTimestampByPerson(PersonJDBCDao.getInstance().getPersonByName("Test","Person")).getFirst()));
    }

    @Test
    @Order(4)
    void getTimestampByPerson() throws SQLException {
        assertNotNull(TimestampJDBCDao.getInstance().getTimestampByPerson(PersonJDBCDao.getInstance().getPersonByName("Test","Person")));
    }

    @Test
    @Order(5)
    void getTimestampByPersonByDate() throws SQLException {
        assertNotNull(TimestampJDBCDao.getInstance().getTimestampByPersonByDate(PersonJDBCDao.getInstance().getPersonByName("Test","Person"),LocalDate.of(2024,1,1)));
    }

    @Test
    @Order(6)
    void removeTimestampWithId() throws SQLException {
        System.out.println(PersonJDBCDao.getInstance().getPersonByName("Test","Person"));
        TimestampJDBCDao.getInstance().removeTimestampWithId(TimestampJDBCDao.getInstance().getTimestampByPerson(PersonJDBCDao.getInstance().getPersonByName("Test","Person")).getFirst().getId());
        assertNull(TimestampJDBCDao.getInstance().getTimestampByPerson(PersonJDBCDao.getInstance().getPersonByName("Test","Person")));
    }

    @AfterAll
    static void after() throws SQLException {
        PersonJDBCDao.getInstance().removePersonWithId(PersonJDBCDao.getInstance().getPersonByName("Test","Person").getId());
    }
}