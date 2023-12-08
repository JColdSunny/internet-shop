package com.vstu.internetshop.controller;

import com.vstu.internetshop.util.JavaFxUtil;
import javafx.event.ActionEvent;

public class AdminController {

    public void productsBtnOnAction(ActionEvent e) {
        JavaFxUtil.moveToPage(e, "admin-products.fxml");
    }

    public void backBtnOnAction(ActionEvent e) {
        JavaFxUtil.moveToPage(e, "main.fxml");
    }

    public void ordersBtnOnAction(ActionEvent e) {
        JavaFxUtil.moveToPage(e, "admin-orders.fxml");
    }

    public void usersBtnOnAction(ActionEvent e) {
        JavaFxUtil.moveToPage(e, "admin-users.fxml");
    }

}
