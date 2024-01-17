package org.example.zeitrechner;

import java.sql.SQLException;

public class Test {
    public static void main(String[] args) {
        PersonJDBCDao personJDBCDao = new PersonJDBCDao();
        try {
            System.out.println(personJDBCDao.getAllPerson().get(1).getFullName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
