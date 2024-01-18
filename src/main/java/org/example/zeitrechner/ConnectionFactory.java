package org.example.zeitrechner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Diese Klasse ist dazu da, eine Verbindung mit der Datenbank aufzubauen.
 * @author Matthias Baumgartner
 *
 */

public class ConnectionFactory {

    private static ConnectionFactory instanz = new ConnectionFactory("plantdex.ch","ZeitrechnerUser","6O2sTjgCWKclKkYs");

    private String url;
    private String user;
    private String passwort;

    public ConnectionFactory(String url, String user, String passwort) {
        this.url = url;
        this.user = user;
        this.passwort = passwort;
    }

    /**
     * Diese Methode gibt die Verbindung zur Datenbank aus.
     */
    public Connection getConnection() {
        String jdcbUrl = "jdbc:mysql://" + url + ":3306/ZEITRECHNER";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(jdcbUrl, user, passwort);
        } catch (SQLException e) {
            System.out.println("An SQL exception occurred:");
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * Diese Methode gibt immer die gleiche Instanz der ConnectionFactory aus.
     */
    public static ConnectionFactory getInstance() {
        return instanz;
    }
}

