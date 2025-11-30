package dev.loteria.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1100, 700);
        scene.getStylesheets().add(getClass().getResource("/css/dark-theme.css").toExternalForm());
        stage.setTitle("Loteria - Dashboard");
        stage.setScene(scene);
        stage.show();
    }
}
