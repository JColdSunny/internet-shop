package com.vstu.internetshop.exception;

import java.sql.SQLException;

/**
 * Этот класс является пользовательским исключением, которое преобразует SQLException в RuntimeException.
 * Используется для хэндлинга JDBC исключений.
 */
public class JdbcException extends RuntimeException {

    /**
     * Конструктор для JdbcException.
     *
     * @param message сообщение об ошибке
     * @param cause исключение-причина
     */
    public JdbcException(String message, SQLException cause) {
        super(message, cause);
    }

}