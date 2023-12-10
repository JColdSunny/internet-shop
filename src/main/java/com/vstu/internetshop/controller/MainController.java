package com.vstu.internetshop.controller;

import com.vstu.internetshop.dao.OrderDao;
import com.vstu.internetshop.dao.ProductDao;
import com.vstu.internetshop.dto.ProductFilter;
import com.vstu.internetshop.dto.ProductStatus;
import com.vstu.internetshop.entity.OrderEntity;
import com.vstu.internetshop.entity.ProductEntity;
import com.vstu.internetshop.util.JavaFxUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import static com.vstu.internetshop.service.UserSession.SESSION;

public class MainController implements Initializable {

    private final ProductDao productDao = new ProductDao();
    private final OrderDao orderDao = new OrderDao();

    @FXML
    private TableView<ProductEntity> productsTable;

    @FXML
    private TableColumn<ProductEntity, String> descriptionColumn;

    @FXML
    private TableColumn<ProductEntity, String> nameColumn;

    @FXML
    private TableColumn<ProductEntity, BigDecimal> priceColumn;

    @FXML
    private Button adminBtn;

    @FXML
    private Label userLabel;

    @Override
    /**
     * Инициализация контроллера
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        fulfillTable();

        if (SESSION.getUser().getRoles().stream().anyMatch(role -> "ADMIN".equals(role.getName()))) {
            adminBtn.setDisable(false);
            adminBtn.setOpacity(1);
        }
        userLabel.setText("%s %s!".formatted(SESSION.getUser().getFirstname(), SESSION.getUser().getLastname()));
    }

    /**
     * Нажатие кнопки истории заказов
     */
    public void ordersHistoryBtnOnAction(ActionEvent e) {
        JavaFxUtil.moveToPage(e, "user-order-history.fxml");
    }

    /**
     * Нажатие кнопки текущего заказа
     */
    public void currentOrderBtnOnAction(ActionEvent e) {
        if (SESSION.getOrder() == null) {
            return;
        }
        JavaFxUtil.moveToPage(e, "user-order.fxml");
    }

    /**
     * Добавление товара в заказ
     */
    public void addToOrderOnAction(ActionEvent e) {
        ProductEntity product = productsTable.getSelectionModel().getSelectedItem();
        if (product == null) {
            return;
        }
        if (SESSION.getOrder() == null) {
            OrderEntity order = orderDao.createOrder(SESSION.getUser())
                    .orElseThrow(() -> new RuntimeException("Failed to get order"));
            SESSION.setOrder(order);
        }
        productDao.addProductToOrder(product, SESSION.getOrder());
        fulfillTable();
    }

    /**
     * Нажатие кнопки выхода
     */
    public void logoutBtnOnAction(ActionEvent e) {
        SESSION.setUser(null);
        SESSION.setOrder(null);
        JavaFxUtil.moveToPage(e, "login.fxml");
    }

    /**
     * Нажатие кнопки администратора
     */
    public void adminBtnOnAction(ActionEvent e) {
        JavaFxUtil.moveToPage(e, "admin.fxml");
    }

    /**
     * Заполение таблицы продуктов
     */
    private void fulfillTable() {
        ProductFilter filter = new ProductFilter();
        filter.setStatus(ProductStatus.AVAILABLE);
        productsTable.setItems(FXCollections.observableList(productDao.getProducts(filter)));
    }

}