package dev.loteria.gui.controllers;

import dev.loteria.models.Modalidade;
import dev.loteria.models.Sorteio;
import dev.loteria.dao.ModalidadeDao;
import dev.loteria.services.SorteioService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
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
    configureComboPresentation();
    loadModalidades();
  }

  private void configureComboPresentation() {
    StringConverter<Modalidade> converter = new StringConverter<>() {
      @Override
      public String toString(Modalidade modalidade) {
        return modalidade == null ? "" : modalidade.getNome();
      }

      @Override
      public Modalidade fromString(String nome) {
        if (nome == null || nome.isBlank())
          return null;
        return modalidadeCombo.getItems().stream()
            .filter(m -> nome.equals(m.getNome()))
            .findFirst()
            .orElse(null);
      }
    };

    modalidadeCombo.setConverter(converter);
    modalidadeCombo.setCellFactory(listView -> new ListCell<>() {
      @Override
      protected void updateItem(Modalidade item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty || item == null ? "" : item.getNome());
      }
    });
    modalidadeCombo.setButtonCell(new ListCell<>() {
      @Override
      protected void updateItem(Modalidade item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty || item == null ? "" : item.getNome());
      }
    });
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
      showAnimation(s);
      if (onSaved != null)
        onSaved.accept(s);
      closeWindow();
    } catch (Exception e) {
      titleLabel.setText("Erro ao realizar sorteio: " + e.getMessage());
    }
  }

  private void showAnimation(Sorteio sorteio) {
    if (sorteio == null)
      return;
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/sorteio_animation.fxml"));
      Parent root = loader.load();
      Scene scene = new Scene(root, 960, 520);
      scene.getStylesheets().add(getClass().getResource("/css/dark-theme.css").toExternalForm());
      SorteioAnimationController controller = loader.getController();
      controller.setSorteio(sorteio);
      Stage stage = new Stage();
      stage.setTitle("Sorteio em andamento");
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initOwner(btnSave.getScene().getWindow());
      stage.setMinWidth(960);
      stage.setMinHeight(520);
      stage.setResizable(false);
      stage.setScene(scene);
      stage.showAndWait();
    } catch (IOException e) {
      System.err.println("Erro ao exibir animação do sorteio: " + e.getMessage());
    }
  }

  private void closeWindow() {
    Stage stage = (Stage) btnCancel.getScene().getWindow();
    stage.close();
  }
}
