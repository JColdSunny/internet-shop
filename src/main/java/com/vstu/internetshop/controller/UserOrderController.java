package com.vstu.internetshop.controller;

import com.vstu.internetshop.dao.OrderDao;
import com.vstu.internetshop.dao.ProductDao;
import com.vstu.internetshop.dto.OrderStatus;
import com.vstu.internetshop.dto.ProductFilter;
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
import java.util.List;
import java.util.ResourceBundle;

import static com.vstu.internetshop.service.UserSession.SESSION;

/**
 * Класс контроллера для управления заказами пользователей.
 */
public class UserOrderController implements Initializable {

    /**
     * DAO для продуктов.
     */
    private final ProductDao productDao = new ProductDao();

    /**
     * DAO для заказов.
     */
    private final OrderDao orderDao = new OrderDao();

    @FXML
    private TableColumn<ProductEntity, String> descriptionColumn;

    @FXML
    private TableColumn<ProductEntity, String> nameColumn;

    @FXML
    private Label orderPriceLabel;

    @FXML
    private TableColumn<ProductEntity, BigDecimal> priceColumn;

    @FXML
    private TableView<ProductEntity> productTable;

    @FXML
    private Button payOrderBtn;

    @Override
    /**
     * Метод выполняемый при инициализации контроллера.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        fulfillScene();
    }

    /**
     * Метод выполняемый при нажатии на кнопку удаления продукта из заказа.
     */
    public void removeProductBtnOnAction(ActionEvent e) {
        ProductEntity product = productTable.getSelectionModel().getSelectedItem();
        if (product == null) {
            return;
        }
        productDao.removeProductFromOrder(product);
        fulfillScene();
    }

    /**
     * Метод выполняемый при нажатии на кнопку отмены.
     */
    public void cancelBtnOnAction(ActionEvent e) {
        JavaFxUtil.moveToPage(e, "main.fxml");
    }

    /**
     * Метод выполняемый при нажатии на кнопку отмены заказа.
     */
    public void cancelOrderBtnOnAction(ActionEvent e) {
        productTable.getItems().forEach(productDao::removeProductFromOrder);
        orderDao.updateOrder(SESSION.getOrder().getId(), OrderStatus.CANCELED);
        SESSION.setOrder(null);
        JavaFxUtil.moveToPage(e, "main.fxml");
    }

    /**
     * Метод выполняемый при нажатии на кнопку оплаты заказа.
     */
    public void payOrderBtnOnAction(ActionEvent e) {
        orderDao.updateOrder(SESSION.getOrder().getId(), OrderStatus.PROCESSING);
        SESSION.setOrder(null);
        JavaFxUtil.moveToPage(e, "main.fxml");
    }

    /**
     * Заполняет таблицу продуктов, выставляет общую стоимость заказа.
     */
    private void fulfillScene() {
        ProductFilter filter = new ProductFilter();
        filter.setOrderId(SESSION.getOrder().getId());
        List<ProductEntity> products = productDao.getProducts(filter);
        productTable.setItems(FXCollections.observableList(products));

        String price = products.stream()
                .map(ProductEntity::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add).toString();
        orderPriceLabel.setText(price);

        if (products.isEmpty()) {
            payOrderBtn.setDisable(false);
        }
    }
}