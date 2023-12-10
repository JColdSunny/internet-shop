package com.vstu.internetshop.dao;

import com.vstu.internetshop.dto.OrderFilter;
import com.vstu.internetshop.dto.OrderStatus;
import com.vstu.internetshop.entity.OrderEntity;
import com.vstu.internetshop.entity.UserEntity;
import com.vstu.internetshop.exception.JdbcException;
import com.vstu.internetshop.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Класс слоя данных реализующий работу с заказами.
 */
public class OrderDao {

    /**
     * SQL-запрос для получения заказов.
     */
    private static final String GET_ORDERS_SQL = """
            SELECT
                o.id as order_id,
                user_id,
                status,
                created_at,
                username
            FROM orders o
            LEFT JOIN users u ON u.id = user_id
            """;

    /**
     * SQL-запрос для получения заказа по id.
     */
    private static final String GET_ORDER_BY_ID_SQL = GET_ORDERS_SQL + " WHERE o.id = ?";

    /**
     * SQL-запрос для создания заказа.
     */
    private static final String CREATE_ORDER_SQL = """
            INSERT INTO orders (user_id, status)
            VALUES (?, ?)
            """;

    /**
     * SQL-запрос для получения id последнего созданного заказа.
     */
    private static final String GET_INSERTED_ID_SQL = """
            SELECT MAX(id) as order_id FROM orders
            """;

    /**
     * SQL-запрос для обновления заказа.
     */
    private static final String UPDATE_ORDER_SQL = """
            UPDATE orders
            SET status = ?
            WHERE id = ?
            """;

    /**
     * Возвращает список заказов, отфильтрованных по переданным критериям.
     *
     * @param filter Критерии фильтрации.
     * @return Список заказов.
     * @throws JdbcException в случае ошибки выполнения SQL-запроса.
     */
    public List<OrderEntity> getOrders(OrderFilter filter) {
        String sql = GET_ORDERS_SQL;
        if (filter != null) {
            sql += " WHERE ";
            if (filter.getUserId() != null) {
                sql += " user_id = %s ".formatted(filter.getUserId());
            }
        }

        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<OrderEntity> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(buildOrderEntity(resultSet));
            }
            return orders;
        } catch (SQLException e) {
            throw new JdbcException("Failed to get orders", e);
        }
    }

    /**
     * Создаёт новый заказ для указанного пользователя.
     *
     * @param user Пользователь, для которого создаётся заказ.
     * @return Созданный заказ.
     * @throws JdbcException в случае ошибки выполнения SQL-запроса.
     */
    public Optional<OrderEntity> createOrder(UserEntity user) {
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement createOrderStatement = connection.prepareStatement(CREATE_ORDER_SQL);
             PreparedStatement getInsertedIdStatement = connection.prepareStatement(GET_INSERTED_ID_SQL);
             PreparedStatement getOrderByIdStatement = connection.prepareStatement(GET_ORDER_BY_ID_SQL)) {
            createOrderStatement.setInt(1, user.getId());
            createOrderStatement.setString(2, OrderStatus.CREATED.toString());
            createOrderStatement.executeUpdate();

            ResultSet resultSet = getInsertedIdStatement.executeQuery();
            if (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                getOrderByIdStatement.setInt(1, orderId);
                ResultSet orderResultSet = getOrderByIdStatement.executeQuery();
                if (orderResultSet.next()) {
                    OrderEntity orderEntity = buildOrderEntity(orderResultSet);
                    orderEntity.setUser(user);
                    return Optional.of(orderEntity);
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new JdbcException("Failed to create order", e);
        }
    }

    /**
     * Обновляет статус указанного заказа.
     *
     * @param orderId ID заказа.
     * @param status  Новый статус заказа.
     * @return true, если статус заказа был успешно обновлён, иначе false.
     * @throws JdbcException в случае ошибки выполнения SQL-запроса.
     */
    public boolean updateOrder(Integer orderId, OrderStatus status) {
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_ORDER_SQL)) {
            statement.setString(1, status.toString());
            statement.setInt(2, orderId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new JdbcException("Failed to update order", e);
        }
    }

    private OrderEntity buildOrderEntity(ResultSet resultSet) throws SQLException {
        UserEntity user = new UserEntity();
        user.setId(resultSet.getInt("user_id"));
        user.setUsername(resultSet.getString("username"));

        return new OrderEntity(
                resultSet.getInt("order_id"),
                user,
                OrderStatus.fromString(resultSet.getString("status")).orElse(null),
                resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }

}
