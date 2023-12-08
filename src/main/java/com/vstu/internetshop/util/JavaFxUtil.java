package com.vstu.internetshop.util;

import com.vstu.internetshop.InternetShopApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class JavaFxUtil {

    private JavaFxUtil() {
        throw new UnsupportedOperationException("This is an utility class");
    }

    public static void moveToPage(ActionEvent e, String file) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(InternetShopApplication.class.getResource(file));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setTitle("Marketplace");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load %s".formatted(file), ex);
        }
    }
}
