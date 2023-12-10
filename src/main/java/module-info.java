module com.vstu.internetshop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.vstu.internetshop to javafx.fxml;
    exports com.vstu.internetshop;
    exports com.vstu.internetshop.controller;
    exports com.vstu.internetshop.entity;
    exports com.vstu.internetshop.dao;
    exports com.vstu.internetshop.mapper;
    exports com.vstu.internetshop.dto;
    opens com.vstu.internetshop.controller to javafx.fxml;
}