package com.vstu.internetshop.util;

import com.vstu.internetshop.exception.JdbcException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Это утилитарный класс предназначенный для управления соединениями с базой данных.
 */
public final class ConnectionManager {
    /**
     * Константа содержит ключ для получения URL базы данных из свойств приложения.
     */
    private static final String DB_URL_PROPERTY = "database.url";

    // Закрытый конструктор для предотвращения создания объектов этого класса
    private ConnectionManager() {
        throw new UnsupportedOperationException("This is an utility class");
    }

     /**
     * Метод для установки соединения с базой данных.
     *
     * @return Connection объект соединения с базой данных
     * @throws JdbcException при возникновении проблем с установкой соединения
     */
    public static Connection openConnection() {
        try {
            return DriverManager.getConnection(ApplicationPropertiesUtil.get(DB_URL_PROPERTY));
        } catch (SQLException e) {
            throw new JdbcException("Failed to get connection to the database", e);
        }
    }

}