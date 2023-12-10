/**
 * Контроллер для обработки заказов курьером
 */
package com.vstu.internetshop.controller;

import com.vstu.internetshop.dao.OrderDao;
import com.vstu.internetshop.dto.OrderDto;
import com.vstu.internetshop.dto.OrderStatus;
import com.vstu.internetshop.entity.OrderEntity;
import com.vstu.internetshop.mapper.OrderMapper;
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

import static com.vstu.internetshop.service.UserSession.SESSION;

public class CourierOrdersController implements Initializable {
    private final OrderDao orderDao = new OrderDao();
    private final OrderMapper orderMapper = new OrderMapper();

    @FXML
    private TableColumn<OrderDto, Integer> numberColumn;

    @FXML
    private TableView<OrderDto> orderTable;

    @FXML
    private TableColumn<OrderDto, String> statusColumn;

    @FXML
    private TableColumn<OrderDto, String> userColumn;

    /**
     * Обработчик события для кнопки завершения доставки
     */
    public void endDevilaryBtnOnAction(ActionEvent e) {
        OrderDto order = orderTable.getSelectionModel().getSelectedItem();
        if (order == null) {
            return;
        }
        if (!"В пути".equals(order.getStatus())) {
            return;
        }
        orderDao.updateOrder(order.getId(), OrderStatus.DELIVERED);
        fulfillScene();
    }

    /**
     * Обработчик события для кнопки выхода из системы
     */
    public void exitBtnOnAction(ActionEvent e) {
        SESSION.setUser(null);
        JavaFxUtil.moveToPage(e, "login.fxml");
    }

    /**
     * Обработчик события для кнопки начала доставки
     */
    public void startDevilaryBtnOnAction(ActionEvent e) {
        OrderDto order = orderTable.getSelectionModel().getSelectedItem();
        if (order == null) {
            return;
        }
        if (!"Зарегестрирован".equals(order.getStatus())) {
            return;
        }
        orderDao.updateOrder(order.getId(), OrderStatus.IN_DELIVERY);
        fulfillScene();
    }

    /**
     * Метод для инициализации контроллера
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        fulfillScene();
    }

    /**
     * Метод для заполнения интерфейса данными
     */
    private void fulfillScene() {
        List<OrderDto> orders = orderDao.getOrders(null).stream()
                .filter(this::applyOrderStatus)
                .map(orderMapper::toDto)
                .toList();
        orderTable.setItems(FXCollections.observableArrayList(orders));
    }

    /**
     * Метод для фильтрации заказов по статусу
     */
    private boolean applyOrderStatus(OrderEntity order) {
        return OrderStatus.CONFIRMED.equals(order.getStatus())
               || OrderStatus.IN_DELIVERY.equals(order.getStatus())
               || OrderStatus.DELIVERED.equals(order.getStatus());
    }
}