package com.vstu.internetshop.controller;

import com.vstu.internetshop.dao.OrderDao;
import com.vstu.internetshop.dao.ProductDao;
import com.vstu.internetshop.dto.OrderFilter;
import com.vstu.internetshop.dto.OrderHistoryDto;
import com.vstu.internetshop.dto.ProductFilter;
import com.vstu.internetshop.entity.OrderEntity;
import com.vstu.internetshop.entity.ProductEntity;
import com.vstu.internetshop.mapper.OrderHistoryMapper;
import com.vstu.internetshop.util.JavaFxUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.vstu.internetshop.service.UserSession.SESSION;

public class UserOrderHistoryController implements Initializable {
    private final ProductDao productDao = new ProductDao();
    private final OrderDao orderDao = new OrderDao();
    private final OrderHistoryMapper orderHistoryMapper = new OrderHistoryMapper();

    @FXML
    private TableColumn<OrderHistoryDto, Integer> countColumn;

    @FXML
    private TableView<OrderHistoryDto> orderTable;

    @FXML
    private TableColumn<OrderHistoryDto, BigDecimal> priceColumn;

    @FXML
    private TableColumn<OrderHistoryDto, String> statusColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        OrderFilter orderFilter = new OrderFilter();
        orderFilter.setUserId(SESSION.getUser().getId());
        List<OrderEntity> orders = orderDao.getOrders(orderFilter);

        List<OrderHistoryDto> orderHistoryList = orders.stream()
                .map(this::buildOrderHistoryDto)
                .toList();
        orderTable.setItems(FXCollections.observableList(orderHistoryList));
    }

    public void cancelBtnOnAction(ActionEvent e) {
        JavaFxUtil.moveToPage(e, "main.fxml");
    }

    private OrderHistoryDto buildOrderHistoryDto(OrderEntity order) {
        ProductFilter filter = new ProductFilter();
        filter.setOrderId(order.getId());
        List<ProductEntity> products = productDao.getProducts(filter);
        return orderHistoryMapper.toDto(order, products);
    }

}
