package com.vstu.internetshop.controller;

import com.vstu.internetshop.dao.ProductDao;
import com.vstu.internetshop.dto.ProductDto;
import com.vstu.internetshop.mapper.ProductMapper;
import com.vstu.internetshop.util.JavaFxUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Класс контроллер для администратора продуктов.
 */
public class AdminProductsController implements Initializable {
    private final ProductDao productDao = new ProductDao();
    private final ProductMapper productMapper = new ProductMapper();

    @FXML
    private TableColumn<ProductDto, String> createdByColumn;

    @FXML
    private TableColumn<ProductDto, String> descriptionColumn;

    @FXML
    private TableColumn<ProductDto, String> nameColumn;

    @FXML
    private TableColumn<ProductDto, BigDecimal> priceColumn;

    @FXML
    private TableView<ProductDto> productsTable;

    @FXML
    private TableColumn<ProductDto, String> statusColumn;

    /**
     * Список продуктов.
     */
    private final ObservableList<ProductDto> products = FXCollections.observableList(productDao.getProducts(null).stream()
            .map(productMapper::toDto)
            .toList());

    /**
     * Обрабатывание события для кнопки отмены.
     * @param e действие
     */
    public void cancelBtnOnAction(ActionEvent e) {
        JavaFxUtil.moveToPage(e, "admin.fxml");
    }

    /**
     * Обрабатывание события для кнопки добавить продукт.
     * @param e действие
     */
    public void addProductOnAction(ActionEvent e) {
        JavaFxUtil.moveToPage(e, "admin-create-product.fxml");
    }

    /**
     * Инициализация таблицы и столбцов в ней.
     * @param url URL
     * @param resourceBundle ресурс
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        createdByColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        productsTable.setItems(products);
    }
}