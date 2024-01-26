package org.example.zeitrechner;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

/**
 * Die Klasse, um mit der Tabelle "Person" zu interargieren
 */
public class TimestampJDBCDao implements TimestampDao{
    final private static Connection connection = ConnectionFactory.getInstance().getConnection();
    final static private TimestampJDBCDao instance = new TimestampJDBCDao();
    /**
     * Einen Timestamp erstelln und einfügen
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
     * Einen bestehenden Timestamp einfügen, entweder mit bestimmter ID oder auto_increment ID
     * @param timestamp der Timestamp
     * @throws SQLException Wenn etwas bei der Datenbank schiefläuft
     */
    @Override
    public void insertTimestamp(Timestamp timestamp) throws SQLException {
        String sql;
        if (timestamp.getId()==0) {sql = "INSERT INTO TIMESTAMP VALUES (NULL, " + timestamp.getPerson().getId() + ",'" + java.sql.Date.valueOf(timestamp.getDate()) + "', " + timestamp.getSek() + ")";}
        else {sql = "INSERT INTO TIMESTAMP VALUES (" + timestamp.getId() + ", " + timestamp.getPerson().getId() + ",'" + java.sql.Date.valueOf(timestamp.getDate()) + "', " + timestamp.getSek() + ")";}
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
        String sql = "SELECT ID_Timestamp, Person_ID, Date, Sekunden FROM TIMESTAMP WHERE ID_Timestamp = " + id + " LIMIT 1;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        return new Timestamp(resultSet.getInt("ID_Timestamp"),personJDBCDao.getPersonById(resultSet.getInt("Person_ID")), resultSet.getDate("Date").toLocalDate(), resultSet.getInt("Sekunden"));
    }

    @Override
    public Timestamp getTimestampById() throws SQLException {
        return getTimestampById(0);
    }

    @Override
    public Timestamp getTimestampByTimestamp(Timestamp timestamp) throws SQLException {
        PersonJDBCDao personJDBCDao = new PersonJDBCDao();
        String sql;
        if (timestamp.getId()==0) {
            sql = "SELECT ID_Timestamp, Person_ID, Date, Sekunden FROM TIMESTAMP WHERE Person_ID=" + timestamp.getPerson().getId() + " AND Date=" + java.sql.Date.valueOf(timestamp.getDate()) + " AND Sekunden=" + timestamp.getSek() + " LIMIT 1;";
        } else {
            sql = "SELECT ID_Timestamp, Person_ID, Date, Sekunden FROM TIMESTAMP WHERE ID_Timestamp = " + timestamp.getId() + " AND Person_ID=" + timestamp.getPerson().getId() + " AND Date=" + java.sql.Date.valueOf(timestamp.getDate()) + " AND Sekunden=" + timestamp.getSek() + " LIMIT 1;";
        }
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            do {
                return new Timestamp(resultSet.getInt("ID_Timestamp"),personJDBCDao.getPersonById(resultSet.getInt("Person_ID")), resultSet.getDate("Date").toLocalDate(), resultSet.getInt("Sekunden"));
            } while (resultSet.next());
        } else {
            return null;
        }
    }

    /**
     * Alle Timestamps einer Person aus der Datenbank ausgeben.
     * @param person Die Person, dessen Timestamps ausgegeben
     * @return Eine Liste mit allen Timestamps der angegebenen Person, wenn es keine gibt null
     * @throws SQLException Wenn etwas bei der Datenbank schiefläuft
     */
    @Override
    public ArrayList<Timestamp> getTimestampByPerson(Person person) throws SQLException {
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
    public ArrayList<Timestamp> getTimestampByPersonByDate(Person person, LocalDate date) throws SQLException {
        PersonJDBCDao personJDBCDao = new PersonJDBCDao();
        Date sqlDate = java.sql.Date.valueOf(date);
        String sql = "SELECT ID_Timestamp,Person_ID,Date,Sekunden FROM TIMESTAMP WHERE Person_ID = " + person.getId() + " AND Date = '" + sqlDate + "';";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        ArrayList<Timestamp> timestampList = new ArrayList<>();
        while (resultSet.next()){
            timestampList.add(new Timestamp(resultSet.getInt("ID_Timestamp"),personJDBCDao.getPersonById(resultSet.getInt("Person_ID")), resultSet.getDate("Date").toLocalDate(), resultSet.getInt("Sekunden")));
        }
        if (!timestampList.isEmpty()) return timestampList;
        else return null;
    }

    public static TimestampJDBCDao getInstance() {
        return instance;
    }
}
