package com.kodilla.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class StoredProcTestSuite {

    private DbManager dbManager;

    @BeforeEach
    void setUp() throws SQLException {
        dbManager = DbManager.getInstance();
    }

    @Test
    public void testUpdateVipLevels() throws SQLException {
        // Given
        String sqlUpdate = "UPDATE READERS SET VIP_LEVEL=\"Not set\"";
        Statement statement = dbManager.getConnection().createStatement();
        statement.executeUpdate(sqlUpdate);
        String sqlCheckTable = "SELECT COUNT(*) AS HOW_MANY FROM READERS WHERE VIP_LEVEL=\"Not set\"";

        // When
        Statement statement2 = dbManager.getConnection().createStatement();
        String sqlProcedureCall = "CALL UpdateVipLevels()";
        statement2.execute(sqlProcedureCall);
        ResultSet rs = statement.executeQuery(sqlCheckTable);

        // Then
        int howMany = -1;
        if (rs.next()) {
            howMany = rs.getInt("HOW_MANY");
        }
        assertEquals(0, howMany);
        rs.close();
        statement.close();
        statement2.close();
    }

    @Test
    public void testUpdateBestsellers() throws SQLException {
        // Given
        Connection conn = dbManager.getConnection();
        Statement statement = conn.createStatement();

        String sqlUpdate = "UPDATE BOOKS SET BESTSELLER = 0";
        statement.executeUpdate(sqlUpdate);

        String sqlCheckBestsellers = "SELECT COUNT(*) AS BESTSELLERS_COUNT FROM BOOKS WHERE BESTSELLER = 1";
        String sqlCountExpectedBestsellers = """
        SELECT COUNT(DISTINCT BOOK_ID) AS EXPECTED_COUNT
        FROM RENTS
        GROUP BY BOOK_ID
        HAVING COUNT(*) >= 2
        """;

        ResultSet rsExpected = statement.executeQuery(sqlCountExpectedBestsellers);
        int expectedBestsellers = 0;
        while (rsExpected.next()) {
            expectedBestsellers++;
        }
        rsExpected.close();

        // When
        Statement statement2 = conn.createStatement();
        String sqlProcedureCall = "CALL UpdateBestsellers()";
        statement2.execute(sqlProcedureCall);

        // Then
        ResultSet rsActual = statement.executeQuery(sqlCheckBestsellers);
        int actualBestsellersCount = -1;
        if (rsActual.next()) {
            actualBestsellersCount = rsActual.getInt("BESTSELLERS_COUNT");
        }

        assertEquals(expectedBestsellers, actualBestsellersCount);

        rsActual.close();
        statement.close();
        statement2.close();
    }
}