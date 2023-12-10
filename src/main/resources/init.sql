-- Создаем таблицу "users", если она не существует
CREATE TABLE IF NOT EXISTS users
(
    id        INTEGER PRIMARY KEY AUTOINCREMENT, -- Идентификатор пользователя
    firstname VARCHAR(50)  NOT NULL,             -- Имя пользователя
    lastname  VARCHAR(50)  NOT NULL,             -- Фамилия пользователя
    username  VARCHAR(50)  NOT NULL UNIQUE,      -- Имя пользователя для входа в систему
    password  VARCHAR(255) NOT NULL,             -- Пароль пользователя
    active    BOOLEAN      NOT NULL DEFAULT true -- Активность пользователя
);


-- Создаем таблицу "roles", если она не существует
CREATE TABLE IF NOT EXISTS roles
(
    id   INTEGER PRIMARY KEY AUTOINCREMENT, -- Идентификатор роли
    name VARCHAR(15) NOT NULL               -- Название роли
);


-- Создаем таблицу "users_roles", если она не существует
CREATE TABLE IF NOT EXISTS users_roles
(
    id      INTEGER PRIMARY KEY AUTOINCREMENT,                                     -- Идентификатор связи
    user_id INTEGER NOT NULL,                                                      -- Идентификатор пользователя
    role_id INTEGER NOT NULL,                                                      -- Идентификатор роли
    CONSTRAINT fk_users_roles_user_id FOREIGN KEY (user_id) REFERENCES users (id), -- Внешний ключ на таблицу users
    CONSTRAINT fk_users_roles_role_id FOREIGN KEY (role_id) REFERENCES roles (id)  -- Внешний ключ на таблицу roles
);


-- Создаем таблицу "orders", если она не существует
CREATE TABLE IF NOT EXISTS orders
(
    id         INTEGER PRIMARY KEY AUTOINCREMENT,                         -- Идентификатор заказа
    user_id    INTEGER     NOT NULL,                                      -- Идентификатор пользователя, сделавшего заказ
    status     VARCHAR(50) NOT NULL,                                      -- Статус заказа (CREATED, PROCESSING, CANCELED, IN_DELIVERY, DELIVERED)
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,            -- Время создания заказа
    CONSTRAINT orders_user_id FOREIGN KEY (user_id) REFERENCES users (id) -- Внешний ключ на таблицу users
);


-- Создаем таблицу "products", если она не существует
CREATE TABLE IF NOT EXISTS products
(
    id          INTEGER PRIMARY KEY AUTOINCREMENT,                                -- Идентификатор продукта
    order_id    INTEGER,                                                          -- Идентификатор заказа
    name        VARCHAR(255)  NOT NULL,                                           -- Название продукта
    price       DECIMAL(8, 2) NOT NULL,                                           -- Цена продукта
    description TEXT          NOT NULL DEFAULT '',                                -- Описание продукта
    created_by  INTEGER       NOT NULL,                                           -- Идентификатор пользователя, который добавил продукт
    status      VARCHAR(50)   NOT NULL,                                           -- Статус продукта (AVAILABLE, IN_ORDER, PAID)
    CONSTRAINT products_order_id FOREIGN KEY (order_id) REFERENCES orders (id),   -- Внешний ключ на таблицу orders
    CONSTRAINT products_created_by FOREIGN KEY (created_by) REFERENCES users (id) -- Внешний ключ на таблицу users
);


-- Добавляем данные в таблицу "roles"
INSERT INTO roles
VALUES (1, 'ADMIN');
INSERT INTO roles
VALUES (2, 'USER');
INSERT INTO roles
VALUES (3, 'COURIER');


-- Добавляем данные в таблицу "users"
INSERT INTO users
VALUES (1, 'Карина', 'Могилек', 'admin', 'admin', true);
INSERT INTO users
VALUES (2, 'Вася', 'Васильев', 'courier', 'courier', true);
INSERT INTO users
VALUES (3, 'Дима', 'Бахасловский', 'user', 'user', true);


-- Добавляем данные в таблицу "users_roles"
INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1);
INSERT INTO users_roles (user_id, role_id)
VALUES (1, 2);
INSERT INTO users_roles (user_id, role_id)
VALUES (2, 3);
INSERT INTO users_roles (user_id, role_id)
VALUES (3, 2);