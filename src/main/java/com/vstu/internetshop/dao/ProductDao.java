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

public class ProductDao {
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
    private static final String CREATE_PRODUCT_SQL = """
            INSERT INTO products (name, price, description, status, created_by)
            VALUES (?, ?, ?, ?, ?)
            """;
    private static final String ADD_PRODUCT_TO_ORDER_SQL = """
            UPDATE products
            SET order_id = ?, status = ?
            WHERE id = ?
            """;
    private static final String REMOVE_PRODUCT_FROM_ORDER_SQL = """
            UPDATE products
            SET order_id = NULL, status = ?
            WHERE id = ?
            """;

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
