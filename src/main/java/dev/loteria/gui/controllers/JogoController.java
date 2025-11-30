package dev.loteria.gui.controllers;

import dev.loteria.dao.JogoDao;
import dev.loteria.dao.ModalidadeDao;
import dev.loteria.dao.ClienteDao;
import dev.loteria.dao.FuncionarioDao;
import dev.loteria.models.Jogo;
import dev.loteria.models.Modalidade;
import dev.loteria.models.Cliente;
import dev.loteria.models.Funcionario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller para listagem e criação de jogos.
 */
public class JogoController {

  @FXML
  private TableView<Jogo> tableView;

  @FXML
  private TableColumn<Jogo, String> colModalidade;

  @FXML
  private TableColumn<Jogo, String> colCliente;

  @FXML
  private TableColumn<Jogo, String> colFuncionario;

  @FXML
  private TableColumn<Jogo, String> colNumeros;

  @FXML
  private TableColumn<Jogo, String> colData;

  @FXML
  private TableColumn<Jogo, Void> colActions;

  @FXML
  private Button btnNovo;

  private JogoDao dao;
  private ModalidadeDao mDao = new ModalidadeDao();
  private ClienteDao cDao = new ClienteDao();
  private FuncionarioDao fDao = new FuncionarioDao();

  public JogoController() {
    try {
      dao = new JogoDao();
    } catch (SQLException e) {
      System.err.println("Erro ao inicializar JogoDao: " + e.getMessage());
    }
  }

  @FXML
  public void initialize() {
    colModalidade.setCellValueFactory(
        j -> new javafx.beans.property.SimpleStringProperty(j.getValue().getModalidade().getNome()));
    colCliente
        .setCellValueFactory(j -> new javafx.beans.property.SimpleStringProperty(j.getValue().getCliente().getNome()));
    colFuncionario.setCellValueFactory(
        j -> new javafx.beans.property.SimpleStringProperty(j.getValue().getFuncionario().getNome()));
    colNumeros.setCellValueFactory(j -> {
      java.util.Set<Integer> nums = j.getValue().getNumeros();
      String joined = "";
      if (nums != null && !nums.isEmpty()) {
        joined = nums.stream()
            .map(Object::toString)
            .collect(java.util.stream.Collectors.joining("-"));
      }
      return new javafx.beans.property.SimpleStringProperty(joined);
    });
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    colData.setCellValueFactory(
        j -> new javafx.beans.property.SimpleStringProperty(j.getValue().getDataCompra().format(fmt)));

    colActions.setCellFactory(col -> new TableCell<Jogo, Void>() {
      private final HBox container = new HBox(8);
      private final Button btnDelete = new Button();

      {
        SVGPath deleteIcon = new SVGPath();
        deleteIcon.setContent("M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z");
        btnDelete.setGraphic(deleteIcon);
        btnDelete.getStyleClass().addAll("action-btn", "action-btn--delete");
        btnDelete.setOnAction(e -> {
          Jogo jogo = getTableView().getItems().get(getIndex());
          if (jogo == null)
            return;
          javafx.scene.control.Alert confirm = new javafx.scene.control.Alert(
              javafx.scene.control.Alert.AlertType.CONFIRMATION);
          confirm.setTitle("Confirmar exclusão");
          confirm.setHeaderText("Excluir jogo");
          confirm.setContentText("Deseja realmente deletar este jogo?");
          java.util.Optional<javafx.scene.control.ButtonType> opt = confirm.showAndWait();
          if (opt.isPresent() && opt.get() == javafx.scene.control.ButtonType.OK) {
            try {
              dao.deletar(jogo.getId());
              refreshTable();
            } catch (SQLException ex) {
              System.err.println("Erro ao deletar jogo: " + ex.getMessage());
            }
          }
        });
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(btnDelete);
      }

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(empty ? null : container);
      }
    });

    btnNovo.setOnAction(e -> openForm(null));
    refreshTable();
  }

  private void refreshTable() {
    if (dao == null)
      return;
    try {
      ResultSet rs = dao.listar();
      if (rs == null) {
        tableView.getItems().clear();
        return;
      }
      List<Jogo> lista = new ArrayList<>();
      while (rs.next()) {
        java.util.UUID id = rs.getObject("id", java.util.UUID.class);
        java.util.UUID mid = rs.getObject("modalidade_id", java.util.UUID.class);
        java.util.UUID cid = rs.getObject("cliente_id", java.util.UUID.class);
        java.util.UUID fid = rs.getObject("funcionario_id", java.util.UUID.class);
        String nums = rs.getString("numeros");
        java.time.LocalDateTime dt = rs.getTimestamp("data_compra").toLocalDateTime();

        Modalidade m = mDao.getById(mid);
        Cliente c = cDao.getById(cid);
        Funcionario f = fDao.getById(fid);
        java.util.Set<Integer> numeros = new java.util.LinkedHashSet<>();
        if (nums != null && !nums.isBlank()) {
          for (String s : nums.split(",")) {
            try {
              numeros.add(Integer.parseInt(s.trim()));
            } catch (NumberFormatException ignore) {
            }
          }
        }
        lista.add(new Jogo(id, m, c, f, numeros, dt));
      }
      try {
        rs.close();
      } catch (SQLException ignore) {
      }
      tableView.getItems().setAll(lista);
    } catch (Exception e) {
      System.err.println("Erro ao carregar jogos: " + e.getMessage());
    }
  }

  private void openForm(Jogo jogo) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/jogo_form.fxml"));
      Scene scene = new Scene(loader.load());
      scene.getStylesheets().add(getClass().getResource("/css/dark-theme.css").toExternalForm());
      JogoFormController controller = loader.getController();
      controller.setJogo(jogo);
      controller.setOnSaved(j -> refreshTable());
      Stage stage = new Stage();
      stage.setTitle(jogo == null ? "Novo Jogo" : "Editar Jogo");
      stage.setScene(scene);
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.showAndWait();
    } catch (IOException ex) {
      System.err.println("Erro ao abrir formulário de jogo: " + ex.getMessage());
    }
  }
}
