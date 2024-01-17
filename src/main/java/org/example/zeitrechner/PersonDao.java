package org.example.zeitrechner;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface PersonDao {

    public void insertPerson(String vorname, String nachname) throws SQLException;

    public List<Person> getAllPerson() throws SQLException;

    public Person getPersonById();

    public Person getPersonByName(String vorname, String Nachname);

    public void removePersonWithId(int id);
}
