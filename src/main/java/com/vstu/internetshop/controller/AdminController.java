package com.vstu.internetshop.controller;

import com.vstu.internetshop.util.JavaFxUtil;
import javafx.event.ActionEvent;

/**
 * Контроллер для обработки действий администратора
 */
public class AdminController {

    /**
     * Обрабатывает нажатие кнопки "Продукты"
     *
     * @param e обозначает действие, которое было произведено
     */
    public void productsBtnOnAction(ActionEvent e) {
        JavaFxUtil.moveToPage(e, "admin-products.fxml");
    }

    /**
     * Обрабатывает нажатие кнопки "Назад"
     *
     * @param e обозначает действие, которое было произведено
     */
    public void backBtnOnAction(ActionEvent e) {
        JavaFxUtil.moveToPage(e, "main.fxml");
    }

    /**
     * Обрабатывает нажатие кнопки "Заказы"
     *
     * @param e обозначает действие, которое было произведено
     */
    public void ordersBtnOnAction(ActionEvent e) {
        JavaFxUtil.moveToPage(e, "admin-orders.fxml");
    }

    /**
     * Обрабатывает нажатие кнопки "Пользователи"
     *
     * @param e обозначает действие, которое было произведено
     */
    public void usersBtnOnAction(ActionEvent e) {
        JavaFxUtil.moveToPage(e, "admin-users.fxml");
    }

}