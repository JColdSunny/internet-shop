CREATE TABLE IF NOT EXISTS users
(
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    firstname VARCHAR(50)  NOT NULL,
    lastname  VARCHAR(50)  NOT NULL,
    username  VARCHAR(50)  NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL,
    active    BOOLEAN      NOT NULL DEFAULT true
);

CREATE TABLE IF NOT EXISTS roles
(
    id   INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(15) NOT NULL
);

CREATE TABLE IF NOT EXISTS users_roles
(
    id      INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,
    CONSTRAINT fk_users_roles_user_id FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_users_roles_role_id FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE IF NOT EXISTS orders
(
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id    INTEGER     NOT NULL,
    status     VARCHAR(50) NOT NULL, -- CREATED, PROCESSING, CANCELED, IN_DELIVERY, DELIVERED
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT orders_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS products
(
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    order_id    INTEGER,
    name        VARCHAR(255)  NOT NULL,
    price       DECIMAL(8, 2) NOT NULL,
    description TEXT          NOT NULL DEFAULT '',
    created_by  INTEGER       NOT NULL,
    status      VARCHAR(50)   NOT NULL, -- AVAILABLE, IN_ORDER, PAID
    CONSTRAINT products_order_id FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT products_created_by FOREIGN KEY (created_by) REFERENCES users (id)
);

INSERT INTO roles
VALUES (1, 'ADMIN'),
       (2, 'USER'),
       (3, 'COURIER');

INSERT INTO users
VALUES (1, 'Карина', 'Могилек', 'admin', 'admin', true),
       (2, 'Вася', 'Васильев', 'courier', 'courier', true),
       (3, 'Дима', 'Бахасловский', 'user', 'user', true);

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (3, 2);
