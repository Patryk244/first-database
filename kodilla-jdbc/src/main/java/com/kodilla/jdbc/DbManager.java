package com.kodilla.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public enum DbManager {
    INSTANCE;

    private Connection conn;

    DbManager() {
        Properties connectionProps = new Properties();
        connectionProps.put("user", "root");
        connectionProps.put("password", "Patryk244");
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/taskmanager" +
                            "?serverTimezone=Europe/Warsaw" +
                            "&useSSL=False",
                    connectionProps);
        } catch (SQLException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static DbManager getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() {
        return conn;
    }

}