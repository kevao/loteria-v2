package dev.loteria.gui.controllers;

import dev.loteria.dao.SorteioDao;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SorteioController {

    @FXML
    private VBox root;

    @FXML
    private Label lblInfo;

    private final SorteioDao dao = new SorteioDao();

    @FXML
    public void initialize() {
        int total = dao.contar();
        lblInfo.setText("Sorteios registrados: " + total);
    }
}
