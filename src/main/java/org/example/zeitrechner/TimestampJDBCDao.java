package org.example.zeitrechner;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Die Klasse, um mit der Tabelle "Person" zu interargieren
 */
public class TimestampJDBCDao implements TimestampDao{
    Connection connection = ConnectionFactory.getInstance().getConnection();

    /**
     * Einen Timestamp einfügen
     * @param person die Person, zu der der Timestamp gehört
     * @param date Datum des Timestamps
     * @param sekunden die Anzahl sekunden seit Tagesbeginn
     * @throws SQLException Wenn etwas bei der Datenbank schiefläuft
     */
    @Override
    public void insertTimestamp(Person person, LocalDate date, int sekunden) throws SQLException {
        Date sqlDate = java.sql.Date.valueOf(date);
        String sql = "INSERT INTO TIMESTAMP VALUES (NULL, " + person.getId() + ",'" + sqlDate + "', " + sekunden + ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();
    }

    /**
     * Einen Timestamp mithilfe seiner ID aus der Datenbank entfernen.
     * @param id die ID des Timestamps
     * @throws SQLException Wenn etwas bei der Datenbank schiefläuft
     */
    @Override
    public void removeTimestampWithId(int id) throws SQLException {
        String sql = "DELETE FROM TIMESTAMP WHERE ID_Timestamp = " + id + " ;";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();
    }

    /**
     * Einn Timestamp mithilfe seiner ID aus der Datenbank ausgeben
     * @param id die ID des Timestamps
     * @return den Timestamp als Instanz von {@link Timestamp}
     * @throws SQLException Wenn etwas bei der Datenbank schiefläuft
     */
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

    /**
     * Alle Timestamps einer Person aus der Datenbank ausgeben.
     * @param person Die Person, dessen Timestamps ausgegeben
     * @return Eine Liste mit allen Timestamps der angegebenen Person, wenn es keine gibt null
     * @throws SQLException Wenn etwas bei der Datenbank schiefläuft
     */
    @Override
    public ArrayList<Timestamp> getTimestampByPerson(Person person) throws SQLException {
        Connection connection = ConnectionFactory.getInstance().getConnection();
        PersonJDBCDao personJDBCDao = new PersonJDBCDao();
        String sql = "SELECT ID_Timestamp,Person_ID,Date,Sekunden FROM TIMESTAMP WHERE Person_ID = " + person.getId() + ";";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        ArrayList<Timestamp> timestampList = new ArrayList<>();
        while (resultSet.next()){
            timestampList.add(new Timestamp(resultSet.getInt("ID_Timestamp"),personJDBCDao.getPersonById(resultSet.getInt("Person_ID")), resultSet.getDate("Date").toLocalDate(), resultSet.getInt("Sekunden")));
        }
        if (!timestampList.isEmpty()) return timestampList;
        else return null;
    }

    /**
     * Alle Timestamps einer Person an einem Datum aus der Datenbank ausgeben.
     * @param person Die Person, dessen Timestamps ausgegeben
     * @param date Das Datum der Timestamps
     * @return Eine Liste mit allen Timestamps der angegebenen Person am angegebenen Datum, wenn es keine gibt null
     * @throws SQLException Wenn etwas bei der Datenbank schiefläuft
     */
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
        if (!timestampList.isEmpty()) return timestampList;
        else return null;
    }
}
