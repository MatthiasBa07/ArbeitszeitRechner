package org.example.zeitrechner;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class TimestampJDBCDao implements TimestampDao{
    Connection connection = ConnectionFactory.getInstance().getConnection();
    @Override
    public void insertTimestamp(Person person, LocalDate date, int sekunden) throws SQLException {
        Date sqlDate = java.sql.Date.valueOf(date);
        String sql = "INSERT INTO TIMESTAMP VALUES (NULL, " + person.getId() + ",'" + sqlDate + "', " + sekunden + ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();
    }

    @Override
    public void removeTimestampWithId(int id) throws SQLException {
        String sql = "DELETE FROM TIMESTAMP WHERE ID_Timestamp = " + id + " ;";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();
    }

    @Override
    public Timestamp getTimestampById(int id) throws SQLException {
        PersonJDBCDao personJDBCDao = new PersonJDBCDao();
        Connection connection = ConnectionFactory.getInstance().getConnection();
        String sql = "SELECT ID_Timestamp, Person_ID, Date, Sekunden FROM TIMESTAMP WHERE ID_Timestamp = " + id + " LIMIT 1;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        return new Timestamp(resultSet.getInt("ID_Timestamp"),personJDBCDao.getPersonById(resultSet.getInt("Person_ID")), resultSet.getDate("Date").toLocalDate(), resultSet.getInt("Sekunden"));
    }

    @Override
    public List<Timestamp> getTimestampByPerson(Person person) throws SQLException {
        Connection connection = ConnectionFactory.getInstance().getConnection();
        PersonJDBCDao personJDBCDao = new PersonJDBCDao();
        String sql = "SELECT ID_Timestamp,Person_ID,Date,Sekunden FROM TIMESTAMP WHERE Person_ID = " + person.getId() + ";";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        List<Timestamp> timestampList = new ArrayList<>();
        while (resultSet.next()){
            timestampList.add(new Timestamp(resultSet.getInt("ID_Timestamp"),personJDBCDao.getPersonById(resultSet.getInt("Person_ID")), resultSet.getDate("Date").toLocalDate(), resultSet.getInt("Sekunden")));
        }
        return timestampList;
    }

    @Override
    public List<Timestamp> getTimestampByPersonByDate(Person person, LocalDate date) throws SQLException {
        Connection connection = ConnectionFactory.getInstance().getConnection();
        PersonJDBCDao personJDBCDao = new PersonJDBCDao();
        Date sqlDate = java.sql.Date.valueOf(date);
        String sql = "SELECT ID_Timestamp,Person_ID,Date,Sekunden FROM TIMESTAMP WHERE Person_ID = " + person.getId() + " AND Date = '" + sqlDate + "';";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        List<Timestamp> timestampList = new ArrayList<>();
        while (resultSet.next()){
            timestampList.add(new Timestamp(resultSet.getInt("ID_Timestamp"),personJDBCDao.getPersonById(resultSet.getInt("Person_ID")), resultSet.getDate("Date").toLocalDate(), resultSet.getInt("Sekunden")));
        }
        return timestampList;
    }
}
