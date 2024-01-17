package org.example.zeitrechner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

    public Connection getConnection() {
        String jdcbUrl = "jdbc:mysql://" + url + ":3306/ZEITRECHNER";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(jdcbUrl, user, passwort);
        } catch (SQLException e) {}
        return conn;
    }

    public static ConnectionFactory getInstance() {
        return instanz;
    }
}

