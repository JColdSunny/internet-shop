package com.vstu.internetshop.controller;

import com.vstu.internetshop.dao.ProductDao;
import com.vstu.internetshop.dto.ProductStatus;
import com.vstu.internetshop.entity.ProductEntity;
import com.vstu.internetshop.service.UserSession;
import com.vstu.internetshop.util.JavaFxUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.math.BigDecimal;

/**
 * Контроллер для создания товара в админ панели
 */
public class AdminCreateProductController {
    private final ProductDao productDao = new ProductDao();

    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private Label fullfillFiledsLabel;

    /**
     * Обработчик клика по кнопке отмены
     * @param e событие
     */
    @FXML
    void cancelBtnOnAction(ActionEvent e) {
        JavaFxUtil.moveToPage(e, "admin-products.fxml");
    }

    /**
     * Обработчик клика по кнопке создания продукта
     * @param e событие
     */
    @FXML
    void createProductBtnOnAction(ActionEvent e) {
        if (isFieldsBlank()) {
            fullfillFiledsLabel.setText("Все обязательные поля должны быть заполнены");
            fullfillFiledsLabel.setOpacity(1);
            return;
        }
        if (!isNumeric(priceTextField.getText())) {
            fullfillFiledsLabel.setText("Цена должно быть числом");
            fullfillFiledsLabel.setOpacity(1);
            return;
        }

        productDao.createProduct(new ProductEntity(
                null,
                nameTextField.getText(),
                new BigDecimal(priceTextField.getText()),
                descriptionTextField.getText(),
                UserSession.SESSION.getUser(),
                ProductStatus.AVAILABLE
        ));
        JavaFxUtil.moveToPage(e, "admin-products.fxml");
    }

    /**
     * Проверяет, являются ли поля пустыми
     * @return true, если поля пустые
     */
    private boolean isFieldsBlank() {
        return nameTextField.getText().isBlank() || priceTextField.getText().isBlank();
    }

    /**
     * Проверяет, является ли строка числом
     * @param str строка для проверки
     * @return true, если строка - число
     */
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}