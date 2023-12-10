package com.vstu.internetshop.dao;

import com.vstu.internetshop.dto.ProductFilter;
import com.vstu.internetshop.dto.ProductStatus;
import com.vstu.internetshop.entity.OrderEntity;
import com.vstu.internetshop.entity.ProductEntity;
import com.vstu.internetshop.entity.UserEntity;
import com.vstu.internetshop.exception.JdbcException;
import com.vstu.internetshop.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы с продуктами в базе данных
 */
public class ProductDao {

/**
 * SQL запрос для получения всех продуктов
 */
private static final String GET_PRODUCTS_SQL = """
        SELECT
            p.id as product_id,
            name,
            status,
            price,
            description,
            u.id as user_id,
            username
        FROM products p
        LEFT JOIN users u ON u.id = p.created_by
        """;

/**
 * SQL запрос для создания нового продукта
 */
private static final String CREATE_PRODUCT_SQL = """
        INSERT INTO products (name, price, description, status, created_by)
        VALUES (?, ?, ?, ?, ?)
        """;
/**
 * SQL запрос для добавления продукта в заказ
 */
private static final String ADD_PRODUCT_TO_ORDER_SQL = """
        UPDATE products
        SET order_id = ?, status = ?
        WHERE id = ?
        """;
/**
 * SQL запрос для удаления продукта из заказа
 */
private static final String REMOVE_PRODUCT_FROM_ORDER_SQL = """
        UPDATE products
        SET order_id = NULL, status = ?
        WHERE id = ?
        """;

/**
 * Метод для получения всех продуктов из БД согласно фильтру
 * @param filter фильтр для выборки продуктов
 * @return Список продуктов
 */
public List<ProductEntity> getProducts(ProductFilter filter) {
    String sql = GET_PRODUCTS_SQL;
    if (filter != null) {
        sql += " WHERE ";
        if (filter.getStatus() != null) {
            sql += " status = '%s' ".formatted(filter.getStatus().toString());
        }
        if (filter.getOrderId() != null) {
            sql += " order_id = %s ".formatted(filter.getOrderId());
        }
    }
    try (Connection connection = ConnectionManager.openConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
        ResultSet resultSet = statement.executeQuery();

        List<ProductEntity> products = new ArrayList<>();
        while (resultSet.next()) {
            products.add(buildProduct(resultSet));
        }
        return products;
    } catch (SQLException e) {
        throw new JdbcException("Failed to get products", e);
    }
}

/**
 * Метод для создания нового продукта
 * @param product продукт, который необходимо создать в БД
 * @return true в случае успешного создания продукта, false - в противном случае
 */
public boolean createProduct(ProductEntity product) {
    try (Connection connection = ConnectionManager.openConnection();
         PreparedStatement statement = connection.prepareStatement(CREATE_PRODUCT_SQL)) {
        statement.setString(1, product.getName());
        statement.setBigDecimal(2, product.getPrice());
        statement.setString(3, product.getDescription());
        statement.setString(4, product.getStatus().toString());
        statement.setInt(5, product.getCreatedUser().getId());
        return statement.executeUpdate() > 0;
    } catch (SQLException e) {
        throw new JdbcException("Failed to create product", e);
    }
}

/**
 * Метод для добавления продукта в заказ в БД
 * @param product продукт, который необходимо добавить в заказ
 * @param order заказ, в который необходимо добавить продукт
 * @return true в случае успешного добавления продукта в заказ, false - в противном случае
 */
public boolean addProductToOrder(ProductEntity product, OrderEntity order) {
    try (Connection connection = ConnectionManager.openConnection();
         PreparedStatement statement = connection.prepareStatement(ADD_PRODUCT_TO_ORDER_SQL)) {
        statement.setInt(1, order.getId());
        statement.setString(2, ProductStatus.IN_ORDER.toString());
        statement.setInt(3, product.getId());
        return statement.executeUpdate() > 0;
    } catch (SQLException e) {
        throw new JdbcException("Failed to add product to order", e);
    }
}

/**
 * Метод для удаления продукта из заказа в БД
 * @param product продукт, который необходимо удалить из заказа
 * @return true в случае успешного удаления продукта из заказа, false - в противном случае
 */
public boolean removeProductFromOrder(ProductEntity product) {
    try (Connection connection = ConnectionManager.openConnection();
         PreparedStatement statement = connection.prepareStatement(REMOVE_PRODUCT_FROM_ORDER_SQL)) {
        statement.setString(1, ProductStatus.AVAILABLE.toString());
        statement.setInt(2, product.getId());
        return statement.executeUpdate() > 0;
    } catch (SQLException e) {
        throw new JdbcException("Failed to remove product from order", e);
    }
}

/**
 * Метод для сборки продукта из данных ResultSet
 * @param resultSet данные о продукте из БД
 * @return объект продукта
 * @throws SQLException в случае возникновения ошибок при чтении данных из БД
 */
private static ProductEntity buildProduct(ResultSet resultSet) throws SQLException {
    UserEntity user = new UserEntity();
    user.setId(resultSet.getInt("user_id"));
    user.setUsername(resultSet.getString("username"));

    return new ProductEntity(
            resultSet.getInt("product_id"),
            resultSet.getString("name"),
            resultSet.getBigDecimal("price"),
            resultSet.getString("description"),
            user,
            ProductStatus.fromString(resultSet.getString("status")).orElse(null)
    );
}

}