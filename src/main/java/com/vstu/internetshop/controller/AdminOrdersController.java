package com.vstu.internetshop.controller;

import com.vstu.internetshop.dao.OrderDao;
import com.vstu.internetshop.dao.ProductDao;
import com.vstu.internetshop.dao.UserDao;
import com.vstu.internetshop.dto.OrderDto;
import com.vstu.internetshop.dto.OrderStatus;
import com.vstu.internetshop.dto.ProductFilter;
import com.vstu.internetshop.mapper.OrderMapper;
import com.vstu.internetshop.service.UserSession;
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

import static com.vstu.internetshop.dto.OrderStatus.CANCELED;
import static com.vstu.internetshop.dto.OrderStatus.CONFIRMED;
import static com.vstu.internetshop.dto.OrderStatus.CREATED;
import static com.vstu.internetshop.dto.OrderStatus.DELIVERED;
import static com.vstu.internetshop.dto.OrderStatus.IN_DELIVERY;
import static com.vstu.internetshop.dto.OrderStatus.PROCESSING;

public class AdminOrdersController implements Initializable {
    private final OrderDao orderDao = new OrderDao();
    private final OrderMapper orderMapper = new OrderMapper();
    private final UserDao userDao = new UserDao();
    private final ProductDao productDao = new ProductDao();

    @FXML
    private TableColumn<OrderDto, String> createdAtColumn;

    @FXML
    private TableView<OrderDto> orderTable;

    @FXML
    private TableColumn<OrderDto, String> statusColumn;

    @FXML
    private TableColumn<OrderDto, String> userColumn;

    /**
     * Обработчик нажатия кнопки "Назад"
     * @param e событие
     */
    public void cancelBtnOnAction(ActionEvent e) {
        JavaFxUtil.moveToPage(e, "admin.fxml");
    }

    /**
     * Обработчик нажатия кнопки "Отмены заказа и добавлние пользоватебя в ЧС"
     * @param e событие
     */
    public void cancelOrderBtnOnAction(ActionEvent e) {
        OrderDto order = orderTable.getSelectionModel().getSelectedItem();
        if (order == null) {
            return;
        }
        if (order.getUserId().equals(UserSession.SESSION.getUser().getId())) {
            return;
        }
        if (!CREATED.equals(mapStatus(order.getStatus()))) {
            return;
        }
        orderDao.updateOrder(order.getId(), CANCELED);
        userDao.updateStatus(order.getUserId(), false);
        ProductFilter filter = new ProductFilter();
        filter.setOrderId(order.getId());
        productDao.getProducts(filter).forEach(productDao::removeProductFromOrder);
        fulfillScene();
    }

    /**
     * Обработчик нажатия кнопки "Зарегистрировать"
     * @param e событие
     */
    public void confirmedBtnOnAction(ActionEvent e) {
        OrderDto order = orderTable.getSelectionModel().getSelectedItem();
        if (order == null) {
            return;
        }
        if (!PROCESSING.equals(mapStatus(order.getStatus()))) {
            return;
        }
        orderDao.updateOrder(order.getId(), CONFIRMED);
        fulfillScene();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        fulfillScene();
    }

    /**
     * Заполняет сцену данными
     */
    private void fulfillScene() {
        List<OrderDto> orders = orderDao.getOrders(null).stream()
                .map(orderMapper::toDto)
                .toList();
        orderTable.setItems(FXCollections.observableArrayList(orders));
    }

    /**
     * Маппер статусов заказа
     * @param status статус заказа в строковом формате
     * @return возвращает статус заказа в константе
     */
    private static OrderStatus mapStatus(String status) {
        return switch (status) {
            case "Не оплачен" -> CREATED;
            case "Оплачен" -> PROCESSING;
            case "Зарегестрирован" -> CONFIRMED;
            case "Отменен" -> CANCELED;
            case "В пути" -> IN_DELIVERY;
            case "Доставлен" -> DELIVERED;
            default -> throw new RuntimeException("Failed to map order status");
        };
    }

}