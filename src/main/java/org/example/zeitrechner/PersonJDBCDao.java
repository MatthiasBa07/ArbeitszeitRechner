package org.example.zeitrechner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse, um mit der Tabelle "Person" zu interargieren
 */
public class PersonJDBCDao implements PersonDao {

    final static private Connection connection = ConnectionFactory.getInstance().getConnection();

    /**
     * Eine Person einfügen
     * @param vorname der Vorname der Person
     * @param nachname der Nachname der Person
     * @throws SQLException Wenn etwas bei der Datenbank schiefläuft
     */
    @Override
    public void insertPerson(String vorname, String nachname) throws SQLException {
        if (vorname.length() < 16 && nachname.length() < 16 && !vorname.isEmpty() && !nachname.isEmpty()) {
            String sql = "INSERT INTO PERSON VALUES (NULL, '" + vorname + "','" + nachname + "')";
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
        }
    }

    /**
     * Alle Personen aus der Datenbank ausgeben.
     * @return Eine Liste mit allen Personen, wenn es keine gibt null
     * @throws SQLException Wenn etwas bei der Datenbank schiefläuft
     */
    @Override
    public List<Person> getAllPerson() throws SQLException {
        Connection connection = ConnectionFactory.getInstance().getConnection();
        String sql = "SELECT ID_Person,Vorname, Nachname FROM PERSON;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        List<Person> personList = new ArrayList<>();

        while (resultSet.next()) {
            personList.add(new Person(resultSet.getInt("ID_Person"), resultSet.getString("Vorname"), resultSet.getString("Nachname")));
        }
        if (!personList.isEmpty()) return personList;
        else return null;
    }

    /**
     * Eine Person mithilfe ihrer ID aus der Datenbank ausgeben
     * @param id die ID der Person
     * @return die Person als Instanz von {@link Person}
     * @throws SQLException Wenn etwas bei der Datenbank schiefläuft
     */
    public Person getPersonById(int id) throws SQLException {
        Connection connection = ConnectionFactory.getInstance().getConnection();
        String sql = "SELECT ID_Person,Vorname, Nachname FROM PERSON WHERE ID_Person = " + id + " LIMIT 1;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        return new Person(resultSet.getInt("ID_Person"),resultSet.getString("Vorname"),resultSet.getString("Nachname"));
    }

    /**
     * Eine Person mit ihrem Vor- und Nachnamen aus der Datenbank ausgeben
     * @param vorname der Vorname der Person
     * @param nachname der Nachname der Person
     * @return die Person als Instanz von {@link Person}
     * @throws SQLException Wenn etwas bei der Datenbank schiefläuft
     */
    @Override
    public Person getPersonByName(String vorname, String nachname) throws SQLException {
        Connection connection = ConnectionFactory.getInstance().getConnection();
        String sql = "SELECT ID_Person,Vorname, Nachname FROM PERSON WHERE Vorname = '" + vorname + "' AND Nachname = '" + nachname + "' LIMIT 1;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        return new Person(resultSet.getInt("ID_Person"),resultSet.getString("Vorname"),resultSet.getString("Nachname"));
    }

    /**
     * Eine Person mithilfe ihrer ID aus der Datenbank entfernen.
     * @param id die ID der Person
     * @throws SQLException Wenn etwas bei der Datenbank schiefläuft
     */
    @Override
    public void removePersonWithId(int id) throws SQLException {
        String sql = "DELETE FROM `PERSON` WHERE ID_Person = " + id + " ;";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();
    }
}
