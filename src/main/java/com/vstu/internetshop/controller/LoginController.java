package com.vstu.internetshop.controller;

import com.vstu.internetshop.dao.UserDao;
import com.vstu.internetshop.entity.UserEntity;
import com.vstu.internetshop.util.JavaFxUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import static com.vstu.internetshop.service.UserSession.SESSION;

public class LoginController {
    private final UserDao userDao = new UserDao();

    @FXML
    private Label loginMsgLabel;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

    public void loginBtnOnAction(ActionEvent e) {
        if (isFieldsBlank()) {
            loginMsgLabel.setText("Введите логин и пароль");
            return;
        }

        UserEntity userEntity = userDao.getUserByUsername(usernameTextField.getText())
                .filter(user -> user.getPassword().equals(passwordTextField.getText()))
                .orElse(null);

        if (userEntity == null) {
            loginMsgLabel.setText("Неверный логин и пароль");
            return;
        }
        if (!userEntity.isActive()) {
            loginMsgLabel.setText("Данный пользовательский аккаунт заблокирован!");
            return;
        }

        SESSION.setUser(userEntity);

        if (userEntity.getRoles().stream().anyMatch(role -> "COURIER".equals(role.getName()))) {
            JavaFxUtil.moveToPage(e, "courier-orders.fxml");
            return;
        }
        JavaFxUtil.moveToPage(e, "main.fxml");
    }

    private boolean isFieldsBlank() {
        return usernameTextField.getText().isBlank() && passwordTextField.getText().isBlank();
    }

}