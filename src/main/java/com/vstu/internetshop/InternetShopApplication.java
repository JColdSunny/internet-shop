package com.vstu.internetshop;

import com.vstu.internetshop.exception.JdbcException;
import com.vstu.internetshop.util.ConnectionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Главный класс приложения Интернет-магазина.
 */
public class InternetShopApplication extends Application {

    /**
     * Инициализация базы данных при запуске приложения.
     */
    @Override
    public void init() {
        initDb();
    }

    /**
     * Запуск приложения.
     *
     * @param stage главная сцена приложения
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InternetShopApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Marketplace");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Главный метод приложения.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Инициализация базы данных.
     */
    private static void initDb() {
        try (Connection connection = ConnectionManager.openConnection()) {
            Statement statement = connection.createStatement();
            if (isDbExists(statement)) {
                return;
            }

            String[] queries = readSqlFile().split(";");

            for (String query : queries) {
                statement.execute(query);
            }

        } catch (SQLException e) {
            throw new JdbcException("Failed init db", e);
        }
    }

    /**
     * Проверка наличия базы данных.
     *
     * @param statement SQL-выражение для выполнения
     * @return true, если база данных существует, иначе false
     */
    private static boolean isDbExists(Statement statement) {
        try {
            statement.executeQuery("SELECT count(*) FROM users");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Чтение SQL-файла.
     *
     * @return содержимое SQL-файла в виде строки
     */
    private static String readSqlFile() {
        try (InputStream is = InternetShopApplication.class.getClassLoader().getResourceAsStream("init.sql")) {
            if (is == null) {
                throw new RuntimeException("SQL file not found");
            }
            return new String(is.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load init db structure", e);
        }
    }

}