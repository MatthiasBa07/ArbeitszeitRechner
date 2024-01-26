package org.example.zeitrechner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Die Klasse, um mit der Tabelle "Person" zu interargieren
 */
public class PersonJDBCDao implements PersonDao {

    final static private Connection connection = ConnectionFactory.getInstance().getConnection();
    final static private PersonJDBCDao instance = new PersonJDBCDao();

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
    public ArrayList<Person> getAllPerson() throws SQLException {
        String sql = "SELECT ID_Person,Vorname, Nachname FROM PERSON;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        ArrayList<Person> personList = new ArrayList<>();

        while (resultSet.next()) {
            personList.add(new Person(resultSet.getInt("ID_Person"), resultSet.getString("Vorname"), resultSet.getString("Nachname")));
        }
        if (!personList.isEmpty()) return personList;
        else return null;
    }

    @Override
    public ArrayList<Person> getAllPerson(String orderBy) throws SQLException {
        String sql = "SELECT ID_Person,Vorname, Nachname FROM PERSON ORDER BY " + orderBy + ";";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        ArrayList<Person> personList = new ArrayList<>();

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
        String sql = "SELECT ID_Person,Vorname, Nachname FROM PERSON WHERE ID_Person = " + id + " LIMIT 1;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            do {
                return new Person(resultSet.getInt("ID_Person"),resultSet.getString("Vorname"),resultSet.getString("Nachname"));
            } while (resultSet.next());
        } else {
            return null;
        }
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
        String sql = "SELECT ID_Person,Vorname, Nachname FROM PERSON WHERE Vorname = '" + vorname + "' AND Nachname = '" + nachname + "' LIMIT 1;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            do {
                return new Person(resultSet.getInt("ID_Person"),resultSet.getString("Vorname"),resultSet.getString("Nachname"));
            } while (resultSet.next());
        } else {
            return null;
        }
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

    public static PersonJDBCDao getInstance() {
        return instance;
    }
}
