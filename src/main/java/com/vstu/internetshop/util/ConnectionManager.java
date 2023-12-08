package com.vstu.internetshop.util;

import com.vstu.internetshop.exception.JdbcException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {
    private static final String DB_URL_PROPERTY = "database.url";

    private ConnectionManager() {
        throw new UnsupportedOperationException("This is an utility class");
    }

    public static Connection openConnection() {
        try {
            return DriverManager.getConnection(ApplicationPropertiesUtil.get(DB_URL_PROPERTY));
        } catch (SQLException e) {
            throw new JdbcException("Failed to get connection to the database", e);
        }
    }

}
