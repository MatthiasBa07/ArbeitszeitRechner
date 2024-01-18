package org.example.zeitrechner;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

public class main {
    public static void main(String[] args) {
        PersonJDBCDao personJDBCDao = new PersonJDBCDao();
        Timestamp time1 = null;
        try {
            time1 = new Timestamp(5,personJDBCDao.getPersonByName("Simon","Gaus"), LocalDate.of(2024, 1, 17),1000);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        time1.makeTimestamp();
        try {
            Thread.sleep(9000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Timestamp time2 = null;
        try {
            time2 = new Timestamp(5,personJDBCDao.getPersonByName("Simon","Gaus"), LocalDate.of(2024, 1, 17),1000);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        time2.makeTimestamp();
        Calculator calculator = new Calculator();
        calculator.sekToTime(calculator.calculateTime(time1.getSek(), time2.getSek()));
    }
}
