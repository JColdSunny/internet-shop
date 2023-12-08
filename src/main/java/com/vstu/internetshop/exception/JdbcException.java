package com.vstu.internetshop.exception;

import java.sql.SQLException;

public class JdbcException extends RuntimeException {

    public JdbcException(String message, SQLException cause) {
        super(message, cause);
    }

}
