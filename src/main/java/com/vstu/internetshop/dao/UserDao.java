package com.vstu.internetshop.dao;

import com.vstu.internetshop.entity.RoleEntity;
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
 * Класс для работы с пользователями в базе данных
 */
public class UserDao {
    // SQL запрос для получения всех пользователей
    private static final String GET_USERS_SQL = """
            SELECT
                u.id as user_id,
                firstname,
                lastname,
                username,
                password,
                active,
                r.id as role_id,
                name
            FROM users u
                LEFT JOIN users_roles ur ON u.id = ur.user_id
                LEFT JOIN roles r ON r.id = ur.role_id
            """;

    // SQL запрос для получения пользователя по имени
    private static final String GET_USER_BY_USERNAME_SQL = GET_USERS_SQL + " WHERE username = ?";

    // SQL запрос для обновления статуса пользователя
    private static final String UPDATE_USER_STATUS_SQL = """
            UPDATE users
            SET active = ?
            WHERE id = ?
            """;

    /**
     * Получить список всех пользователей из базы данных с ролями
     * @return Список пользователей
     */
    public List<UserEntity> getUsers() {
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement statement = connection.prepareStatement(GET_USERS_SQL)) {
            List<UserEntity> users = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                UserEntity user = buildUser(resultSet);
                if (users.contains(user)) {
                    users.stream()
                            .filter(userEntity -> userEntity.getId() == user.getId())
                            .forEach(userEntity -> userEntity.addRole(user.getRoles().get(0)));
                    continue;
                }
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new JdbcException("Failed to get users", e);
        }
    }

    /**
     * Получить пользователя по имени пользователя
     * @param username Имя пользователя
     * @return Пользователь
     */
    public Optional<UserEntity> getUserByUsername(String username) {
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement statement = connection.prepareStatement(GET_USER_BY_USERNAME_SQL)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(buildUser(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new JdbcException("Failed to get users", e);
        }
    }

    /**
     * Обновить статус пользователя
     * @param userId Id пользователя
     * @param isActive Текущий статус пользователя
     * @return признак успешности обновления статуса пользователя
     */
    public boolean updateStatus(Integer userId, boolean isActive) {
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USER_STATUS_SQL)) {
            statement.setBoolean(1, isActive);
            statement.setInt(2, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new JdbcException("Failed to update user status", e);
        }
    }

    /**
     * Создать объект пользователя из результата запроса в базу данных
     * @param resultSet Результат запроса в базу данных
     * @return Объект пользователя
     * @throws SQLException Исключение при ошибке работы с базой данных
     */
    private static UserEntity buildUser(ResultSet resultSet) throws SQLException {
        UserEntity user = new UserEntity(
                resultSet.getInt("user_id"),
                resultSet.getString("firstname"),
                resultSet.getString("lastname"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getBoolean("active")

        );
        RoleEntity role = new RoleEntity(
                resultSet.getInt("role_id"),
                resultSet.getString("name")
        );
        user.addRole(role);
        return user;
    }

}