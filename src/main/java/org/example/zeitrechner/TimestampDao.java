package org.example.zeitrechner;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface TimestampDao {
    public void insertTimestamp(Person person, LocalDate date, int sekunden) throws SQLException;

    public void removeTimestampWithId(int id) throws SQLException;

    public Timestamp getTimestampById(int id) throws SQLException;

    public List<Timestamp> getTimestampByPerson(Person person) throws SQLException;

    public List<Timestamp> getTimestampByPersonByDate(Person person, LocalDate date) throws SQLException;
}
