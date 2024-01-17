package org.example.zeitrechner;

import java.sql.SQLException;
import java.util.List;

public class App {

    public static void main(String[] args) {
        PersonJDBCDao personJDBCDao = new PersonJDBCDao();
        /* Select all
        List<Person> personen = null;
        try {
            personen = personJDBCDao.getAllPerson();
        } catch (
                SQLException ignored) {
        }
        for (Person person : personen) {
            System.out.println(person.getFUllName());
        } */
        /* insert
        try {
            personJDBCDao.insertPerson("Hans","Herman");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
        /* get by name
        try {
            System.out.println(personJDBCDao.getPersonByName("Matthias","Baumgartner").getFullName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
        /* rm
        try {
            personJDBCDao.removePersonWithId(5);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
    }
}
