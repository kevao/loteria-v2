package dev.loteria.ui.gui.controllers;

import dev.loteria.dao.ModalidadeDao;
import dev.loteria.models.Modalidade;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.scene.shape.SVGPath;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

/**
 * Controller limpo para a tela de Modalidades.
 * Mantém: colunas (Nome, Preço, Bolas Sorteadas, Numeração, Descrição, Ações)
 */
public class ModalidadeController {

  @FXML
  private TableView<Modalidade> tableModalidades;

  @FXML
  private TableColumn<Modalidade, String> colNome;

  @FXML
  private TableColumn<Modalidade, String> colValor;

  @FXML
  private TableColumn<Modalidade, Integer> colNumeros;

  @FXML
  private TableColumn<Modalidade, String> colNumeracao;

  @FXML
  private TableColumn<Modalidade, String> colDescricao;

  @FXML
  private TableColumn<Modalidade, Void> colActions;

  @FXML
  private Button btnNew;

  private final ModalidadeDao dao = new ModalidadeDao();

  @FXML
  public void initialize() {
    // Bindings básicos
    colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
    colNumeros.setCellValueFactory(new PropertyValueFactory<>("numerosSorteio"));

    // Numeração: menor-maior com padding 02
    colNumeracao.setCellValueFactory(cell -> {
      Modalidade m = cell.getValue();
      if (m == null)
        return new SimpleStringProperty("");
      return new SimpleStringProperty(String.format("%02d-%02d", m.getMenorBola(), m.getMaiorBola()));
    });

    // Preço em pt-BR
    NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
    colValor.setCellValueFactory(cell -> {
      Modalidade m = cell.getValue();
      if (m == null)
        return new SimpleStringProperty("");
      return new SimpleStringProperty(currency.format(m.getValorJogo()));
    });

    colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

    btnNew.setOnAction(e -> openForm(null));

    // Política de redimensionamento (mantive comportamento anterior)
    tableModalidades.setColumnResizePolicy(javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY);

    // Larguras mínimas
    colNome.setMinWidth(160);
    colValor.setMinWidth(90);
    colNumeros.setMinWidth(80);
    colNumeracao.setMinWidth(90);
    colDescricao.setMinWidth(160);

    // Coluna de ações (ícones)
    colActions.setCellFactory(createActionsCellFactory());

    refreshTable();
  }

  private Callback<TableColumn<Modalidade, Void>, TableCell<Modalidade, Void>> createActionsCellFactory() {
    return param -> new TableCell<>() {
      private final Button editBtn = new Button();
      private final Button delBtn = new Button();
      private final HBox box = new HBox(8, editBtn, delBtn);

      {
        SVGPath pencil = new SVGPath();
        pencil.setContent(
            "M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c0.39-0.39 0.39-1.02 0-1.41l-2.34-2.34c-0.39-0.39-1.02-0.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z");
        pencil.getStyleClass().add("action-icon");
        editBtn.setGraphic(pencil);
        editBtn.getStyleClass().add("action-btn");
        editBtn.setTooltip(new Tooltip("Editar"));

        SVGPath trash = new SVGPath();
        trash.setContent("M6 19c0 1.1 0.9 2 2 2h8c1.1 0 2-0.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z");
        trash.getStyleClass().add("action-icon");
        delBtn.setGraphic(trash);
        delBtn.getStyleClass().addAll("action-btn", "delete");
        delBtn.setTooltip(new Tooltip("Excluir"));

        editBtn.setMinWidth(34);
        editBtn.setPrefWidth(34);
        editBtn.setMinHeight(28);
        delBtn.setMinWidth(34);
        delBtn.setPrefWidth(34);
        delBtn.setMinHeight(28);

        box.setAlignment(Pos.CENTER_RIGHT);
        box.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(box, Priority.ALWAYS);
        setAlignment(Pos.CENTER_RIGHT);

        editBtn.setOnAction(e -> {
          Modalidade m = getTableView().getItems().get(getIndex());
          if (m != null)
            openForm(m);
        });

        delBtn.setOnAction(e -> {
          Modalidade m = getTableView().getItems().get(getIndex());
          if (m == null)
            return;
          Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
          confirm.setTitle("Confirmar exclusão");
          confirm.setHeaderText("Excluir modalidade");
          confirm.setContentText("Deseja realmente excluir a modalidade '" + m.getNome() + "'?");
          Optional<ButtonType> res = confirm.showAndWait();
          if (res.isPresent() && res.get() == ButtonType.OK) {
            dao.deletar(m.getId());
            refreshTable();
          }
        });
      }

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(empty ? null : box);
      }
    };
  }

  private void refreshTable() {
    ObservableList<Modalidade> data = FXCollections.observableArrayList();
    ResultSet rs = null;
    try {
      rs = dao.listar();
      if (rs != null) {
        while (rs.next()) {
          Modalidade m = new Modalidade(
              rs.getObject("id", java.util.UUID.class),
              rs.getString("nome"),
              rs.getInt("numeros_sorteio"),
              rs.getInt("menor_bola"),
              rs.getInt("maior_bola"),
              rs.getDouble("valor_jogo"),
              rs.getString("descricao"));
          data.add(m);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      if (rs != null)
        try {
          rs.close();
        } catch (SQLException ignored) {
        }
    }

    tableModalidades.setItems(data);
  }

  private void openForm(Modalidade modalidade) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modalidade_form.fxml"));
      Parent root = loader.load();
      ModalidadeFormController controller = loader.getController();
      controller.setModalidade(modalidade);
      controller.setOnSaved(m -> refreshTable());

      Stage stage = new Stage();
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.setTitle(modalidade == null ? "Nova Modalidade" : "Editar Modalidade");
      stage.setScene(new Scene(root));
      stage.showAndWait();
    } catch (IOException ex) {
      ex.printStackTrace();
      showAlert("Erro ao abrir o formulário: " + ex.getMessage());
    }
  }

  private void showAlert(String msg) {
    Alert a = new Alert(Alert.AlertType.INFORMATION);
    a.setHeaderText(null);
    a.setContentText(msg);
    a.showAndWait();
  }
}
