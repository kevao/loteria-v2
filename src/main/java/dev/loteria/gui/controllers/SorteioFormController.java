package dev.loteria.gui.controllers;

import dev.loteria.models.Modalidade;
import dev.loteria.models.Sorteio;
import dev.loteria.dao.ModalidadeDao;
import dev.loteria.services.SorteioService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SorteioFormController {

  @FXML
  private ComboBox<Modalidade> modalidadeCombo;

  @FXML
  private Label titleLabel;

  @FXML
  private Button btnCancel;

  @FXML
  private Button btnSave;

  private final ModalidadeDao mDao = new ModalidadeDao();
  private final SorteioService service = new SorteioService();
  private Consumer<Sorteio> onSaved;

  @FXML
  public void initialize() {
    btnCancel.setOnAction(e -> closeWindow());
    btnSave.setOnAction(e -> save());
    loadModalidades();
  }

  private void loadModalidades() {
    try {
      ResultSet rs = mDao.listar();
      List<Modalidade> lista = new ArrayList<>();
      while (rs != null && rs.next()) {
        lista.add(mDao.getById(rs.getObject("id", java.util.UUID.class)));
      }
      if (rs != null)
        try {
          rs.close();
        } catch (SQLException ignore) {
        }
      modalidadeCombo.setItems(FXCollections.observableArrayList(lista));
    } catch (Exception e) {
      System.err.println("Erro ao carregar modalidades: " + e.getMessage());
    }
  }

  public void setOnSaved(Consumer<Sorteio> onSaved) {
    this.onSaved = onSaved;
  }

  private void save() {
    Modalidade m = modalidadeCombo.getValue();
    if (m == null) {
      titleLabel.setText("Escolha uma modalidade");
      return;
    }
    try {
      Sorteio s = service.inserir(m.getId());
      if (onSaved != null)
        onSaved.accept(s);
      closeWindow();
    } catch (Exception e) {
      titleLabel.setText("Erro ao realizar sorteio: " + e.getMessage());
    }
  }

  private void closeWindow() {
    Stage stage = (Stage) btnCancel.getScene().getWindow();
    stage.close();
  }
}
