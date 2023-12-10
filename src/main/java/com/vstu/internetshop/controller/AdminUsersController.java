package com.vstu.internetshop.controller;

import com.vstu.internetshop.dao.UserDao;
import com.vstu.internetshop.dto.UserDto;
import com.vstu.internetshop.mapper.UserMapper;
import com.vstu.internetshop.util.JavaFxUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Класс контролера страницы с пользователями для администратора.
 */
public class AdminUsersController implements Initializable {
    private final UserDao userDao = new UserDao();
    private final UserMapper userMapper = new UserMapper();

    @FXML
    private TableColumn<UserDto, String> activeColumn;

    @FXML
    private TableColumn<UserDto, String> firstnameColumn;

    @FXML
    private TableColumn<UserDto, String> lastnameColumn;

    @FXML
    private TableColumn<UserDto, String> roleColumn;

    @FXML
    private TableView<UserDto> userTable;

    @FXML
    private TableColumn<UserDto, String> usernameColumn;

    /**
     * Обработчик кнопки активации пользователя.
     * @param e Информация о событии.
     */
    public void activeBtnOnAction(ActionEvent e) {
        UserDto user = userTable.getSelectionModel().getSelectedItem();
        if (user == null) {
            return;
        }
        userDao.updateStatus(user.getId(), true);
        fulfillScene();
    }

    /**
     * Обработчик кнопки отмены.
     * @param e Информация о событии.
     */
    public void cancelBtnOnAction(ActionEvent e) {
        JavaFxUtil.moveToPage(e, "admin.fxml");
    }

    /**
     * Инициализация контроллера.
     * @param url Параметр URL.
     * @param resourceBundle Ресурс бандл.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        activeColumn.setCellValueFactory(new PropertyValueFactory<>("active"));
        firstnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("roles"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        fulfillScene();
    }

    /**
     * Заполняет таблицу информации об пользователях.
     */
    private void fulfillScene() {
        List<UserDto> users = userDao.getUsers().stream()
                .map(userMapper::toDto)
                .toList();
        userTable.setItems(FXCollections.observableList(users));
    }
}