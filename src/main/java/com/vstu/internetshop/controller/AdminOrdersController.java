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

    public void cancelBtnOnAction(ActionEvent e) {
        JavaFxUtil.moveToPage(e, "admin.fxml");
    }

    public void cancelOrderBtnOnAction(ActionEvent e) {
        OrderDto order = orderTable.getSelectionModel().getSelectedItem();
        if (order == null) {
            return;
        }
        if (order.getId().equals(UserSession.SESSION.getUser().getId())) {
            return;
        }
        if (!OrderStatus.CREATED.equals(OrderStatus.fromString(order.getStatus()).orElse(null))) {
            return;
        }
        orderDao.updateOrder(order.getId(), OrderStatus.CANCELED);
        userDao.updateStatus(order.getUserId(), false);
        ProductFilter filter = new ProductFilter();
        filter.setOrderId(order.getId());
        productDao.getProducts(filter).forEach(productDao::removeProductFromOrder);
        fulfillScene();
    }

    public void confirmedBtnOnAction(ActionEvent e) {
        OrderDto order = orderTable.getSelectionModel().getSelectedItem();
        if (order == null) {
            return;
        }
        if (!OrderStatus.PROCESSING.equals(OrderStatus.fromString(order.getStatus()).orElse(null))) {
            return;
        }
        orderDao.updateOrder(order.getId(), OrderStatus.CONFIRMED);
        fulfillScene();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        fulfillScene();
    }

    private void fulfillScene() {
        List<OrderDto> orders = orderDao.getOrders(null).stream()
                .map(orderMapper::toDto)
                .toList();
        orderTable.setItems(FXCollections.observableArrayList(orders));
    }
}
