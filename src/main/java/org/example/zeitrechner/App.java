package org.example.zeitrechner;

import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

public class App {

    public static void main(String[] args) {
        PersonJDBCDao personJDBCDao = new PersonJDBCDao();
        TimestampJDBCDao timestampJDBCDao = new TimestampJDBCDao();

        try {

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
