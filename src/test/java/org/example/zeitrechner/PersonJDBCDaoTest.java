package org.example.zeitrechner;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class PersonJDBCDaoTest {
    @Test
    @Order(1)
    void insertPerson() throws SQLException {
        PersonJDBCDao.getInstance().insertPerson("Test","Person");
        Person person = PersonJDBCDao.getInstance().getPersonByName("Test","Person");
        assertNotNull(person);
    }

    @Test
    @Order(2)
    void getPersonByName() throws SQLException {
        Person person = PersonJDBCDao.getInstance().getPersonByName("Test","Person");
        assertNotNull(person);
    }

    @Test
    @Order(3)
    void getPersonById() throws SQLException {
        Person person = PersonJDBCDao.getInstance().getPersonByName("Test","Person");
        Person newPerson = PersonJDBCDao.getInstance().getPersonById(person.getId());
        assertTrue(newPerson.equals(person));
    }

    @Test
    @Order(4)
    void removePersonWithId() throws SQLException {
        Person person = PersonJDBCDao.getInstance().getPersonByName("Test","Person");
        PersonJDBCDao.getInstance().removePersonWithId(person.getId());
        assertNull(PersonJDBCDao.getInstance().getPersonById(person.getId()));
    }
}