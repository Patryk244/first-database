package com.kodilla.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class DbManagerTestSuite {
    private DbManager dbManager;

    @BeforeEach
    void setUp() throws SQLException {
        dbManager = DbManager.getInstance();
    }

    @Test
    void testConnect() throws SQLException {
        assertNotNull(dbManager.getConnection());
    }

    @Test
    void testSelectUser() throws SQLException {
        String query = "SELECT * FROM USERS";
        Statement statement = dbManager.getConnection().createStatement();
        ResultSet rs = statement.executeQuery(query);
        int counter = 0;

        while (rs.next()) {
            System.out.println(rs.getInt("ID") + ", " + rs.getString("FIRSTNAME") + ", " + rs.getString("LASTNAME"));
            counter++;
        }
        rs.close();
        statement.close();
        assertEquals(10, counter);
    }

    @Test
    void testSelectUsersAndTasks() throws SQLException {
        String sqlQuery = """
                SELECT U.FIRSTNAME, U.LASTNAME, I.SUMMARY
                  FROM USERS U
                  JOIN ISSUES I ON U.ID = I.USER_ID
                """;
        Statement statement = dbManager.getConnection().createStatement();
        ResultSet rs = statement.executeQuery(sqlQuery);
        int counter = 0;
        while (rs.next()) {
            System.out.println(rs.getString("FIRSTNAME") + ", " + rs.getString("LASTNAME") + ", " + rs.getString("SUMMARY"));
            counter++;
        }
        rs.close();
        statement.close();
        assertEquals(15, counter);
    }

    @Test
    void testSelectUsersAndPosts() throws SQLException {
        String sqlQuery = """
                SELECT U.FIRSTNAME, U.LASTNAME, COUNT(*) AS POSTS_NUMBER
                FROM USERS U JOIN POSTS P ON U.ID = P.USER_ID
                GROUP BY P.USER_ID
                HAVING COUNT(*) > 1""";
        Statement statement = dbManager.getConnection().createStatement();
        ResultSet rs = statement.executeQuery(sqlQuery);
        int counter = 0;
        while (rs.next()) {
            System.out.println(rs.getString("FIRSTNAME") + ", " + rs.getString("LASTNAME"));
            counter++;
        }
        System.out.println(counter);
        rs.close();
        statement.close();
        assertEquals(3, counter);
    }

}