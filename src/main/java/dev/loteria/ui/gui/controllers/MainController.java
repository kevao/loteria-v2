package dev.loteria.ui.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane root;

    @FXML
    private Button btnModalidades;

    @FXML
    private Button btnSorteios;

    @FXML
    private AnchorPane content;

    @FXML
    public void initialize() {
        // carregar view inicial
        setActive(btnModalidades);
        loadCenter("/fxml/modalidades.fxml");

        btnModalidades.setOnAction(e -> {
            setActive(btnModalidades);
            loadCenter("/fxml/modalidades.fxml");
        });
        btnSorteios.setOnAction(e -> {
            setActive(btnSorteios);
            loadCenter("/fxml/sorteios.fxml");
        });
    }

    private void setActive(Button active) {
        btnModalidades.getStyleClass().remove("active");
        btnSorteios.getStyleClass().remove("active");
        if (!active.getStyleClass().contains("active")) {
            active.getStyleClass().add("active");
        }
    }

    private void loadCenter(String fxml) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(fxml));
            content.getChildren().clear();
            content.getChildren().add(node);
            AnchorPane.setTopAnchor(node, 0.0);
            AnchorPane.setBottomAnchor(node, 0.0);
            AnchorPane.setLeftAnchor(node, 0.0);
            AnchorPane.setRightAnchor(node, 0.0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
