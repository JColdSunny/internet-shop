package com.vstu.internetshop.util;

import com.vstu.internetshop.InternetShopApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Итоговый класс JavaFxUtil.
 * <p>
 * Это класс утилиты. Конструктор приватный для запрета создания экземпляров.
 * </p>
 */
public final class JavaFxUtil {

    /**
     * Приватный конструктор, который бросает исключение.
     * <p>
     * Запрещает создание экземпляров класса утилиты.
     * </p>
     *
     * @throws UnsupportedOperationException если попытаться создать экземпляр.
     */
    private JavaFxUtil() {
        throw new UnsupportedOperationException("This is an utility class");
    }

    /**
     * Перемещает пользователя на другую страницу при событии.
     * <p>
     * Метод подгружает fxml файл, создает новую сцену и устанавливает ее для текущего окна.
     * </p>
     *
     * @param e событие, которое благодаря getSource получает текущее окно.
     * @param file файл fxml для загрузки.
     * @throws RuntimeException если не удалась загрузка файла.
     */
    public static void moveToPage(ActionEvent e, String file) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(InternetShopApplication.class.getResource(file));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setTitle("Marketplace");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load %s".formatted(file), ex);
        }
    }
}
