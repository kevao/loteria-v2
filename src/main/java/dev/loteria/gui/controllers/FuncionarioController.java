package dev.loteria.gui.controllers;

import dev.loteria.dao.FuncionarioDao;
import dev.loteria.dao.JogoDao;
import dev.loteria.models.Funcionario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller para a tela de listagem de Funcionários.
 * Permite visualizar, criar, editar e deletar funcionários.
 */
public class FuncionarioController {

  @FXML
  private TableView<Funcionario> tableView;

  @FXML
  private TableColumn<Funcionario, String> colNome;

  @FXML
  private TableColumn<Funcionario, String> colEmail;

  @FXML
  private TableColumn<Funcionario, String> colMatricula;

  @FXML
  private TableColumn<Funcionario, String> colCargo;

  @FXML
  private TableColumn<Funcionario, Double> colSalario;

  @FXML
  private TableColumn<Funcionario, Boolean> colAtivo;

  @FXML
  private TableColumn<Funcionario, Void> colActions;

  @FXML
  private Button btnNovo;

  private final FuncionarioDao dao = new FuncionarioDao();
  private JogoDao jogoDao;

  public FuncionarioController() {
    try {
      this.jogoDao = new JogoDao();
    } catch (SQLException e) {
      System.err.println("Não foi possível inicializar JogoDao em FuncionarioController: " + e.getMessage());
    }
  }

  /**
   * Inicializa o controller e configura a tabela.
   */
  @FXML
  public void initialize() {
    // Configurar colunas
    colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
    colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
    colCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));

    // Formatar coluna Salário como moeda
    colSalario.setCellValueFactory(new PropertyValueFactory<>("salario"));
    colSalario.setCellFactory(col -> new TableCell<Funcionario, Double>() {
      private final NumberFormat currencyFormat = NumberFormat
          .getCurrencyInstance(java.util.Locale.forLanguageTag("pt-BR"));

      @Override
      protected void updateItem(Double item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
        } else {
          setText(currencyFormat.format(item));
        }
      }
    });

    // Formatar coluna Ativo
    colAtivo.setCellValueFactory(new PropertyValueFactory<>("ativo"));
    colAtivo.setCellFactory(col -> new TableCell<Funcionario, Boolean>() {
      @Override
      protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setGraphic(null);
          setText(null);
        } else {
          SVGPath icon = new SVGPath();
          if (item) {
            // Check icon
            icon.setContent("M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z");
            icon.setStyle("-fx-fill: #4CAF50;");
          } else {
            // X icon
            icon.setContent(
                "M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z");
            icon.setStyle("-fx-fill: #F44336;");
          }
          setGraphic(icon);
          setText(null);
          setAlignment(Pos.CENTER);
        }
      }
    });

    // Configurar coluna de ações com botões de editar e deletar
    colActions.setCellFactory(col -> new TableCell<Funcionario, Void>() {
      private final HBox container = new HBox(8);
      private final Button btnEdit = new Button();
      private final Button btnDelete = new Button();

      {
        // Ícone de editar (lápis)
        SVGPath editIcon = new SVGPath();
        editIcon.setContent(
            "M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z");
        editIcon.getStyleClass().add("icon-edit");
        btnEdit.setGraphic(editIcon);
        btnEdit.getStyleClass().addAll("action-btn", "action-btn--edit");
        btnEdit.setOnAction(e -> {
          Funcionario funcionario = getTableView().getItems().get(getIndex());
          openForm(funcionario);
        });

        // Ícone de deletar (lixeira)
        SVGPath deleteIcon = new SVGPath();
        deleteIcon.setContent(
            "M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z");
        deleteIcon.getStyleClass().add("icon-delete");
        btnDelete.setGraphic(deleteIcon);
        btnDelete.getStyleClass().addAll("action-btn", "action-btn--delete");
        btnDelete.setOnAction(e -> {
          Funcionario funcionario = getTableView().getItems().get(getIndex());
          deleteFuncionario(funcionario);
        });

        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(btnEdit, btnDelete);
      }

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
          setGraphic(null);
        } else {
          setGraphic(container);
        }
      }
    });

    // Botão para novo funcionário
    btnNovo.setOnAction(e -> openForm(null));

    // Carregar dados iniciais
    refreshTable();
  }

  /**
   * Recarrega os dados da tabela a partir do banco de dados.
   */
  private void refreshTable() {
    try {
      ResultSet rs = dao.listar();
      if (rs == null) {
        tableView.getItems().clear();
        return;
      }
      List<Funcionario> funcionarios = new ArrayList<>();
      while (rs.next()) {
        Funcionario f = new Funcionario(
            rs.getObject("id", java.util.UUID.class),
            rs.getString("nome"),
            rs.getString("email"),
            rs.getString("telefone"),
            rs.getString("endereco"),
            rs.getString("cpf"),
            rs.getBoolean("ativo"),
            rs.getString("matricula"),
            rs.getString("cargo"),
            rs.getDouble("salario"));
        funcionarios.add(f);
      }
      try {
        rs.close();
      } catch (SQLException ignore) {
      }
      tableView.getItems().setAll(funcionarios);
    } catch (Exception e) {
      System.err.println("Erro ao carregar funcionários: " + e.getMessage());
    }
  }

  /**
   * Abre o formulário de criação ou edição de funcionário.
   *
   * @param funcionario o funcionário a ser editado, ou null para criar um novo
   */
  private void openForm(Funcionario funcionario) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/funcionario_form.fxml"));
      Scene scene = new Scene(loader.load());
      // Use existing theme from resources
      scene.getStylesheets().add(getClass().getResource("/css/dark-theme.css").toExternalForm());

      FuncionarioFormController controller = loader.getController();
      controller.setFuncionario(funcionario);
      controller.setOnSaved(f -> refreshTable());

      Stage stage = new Stage();
      stage.setTitle(funcionario == null ? "Novo Funcionário" : "Editar Funcionário");
      stage.setScene(scene);
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.showAndWait();
    } catch (IOException e) {
      System.err.println("Erro ao abrir formulário de funcionário: " + e.getMessage());
    }
  }

  /**
   * Deleta um funcionário após confirmação.
   *
   * @param funcionario o funcionário a ser deletado
   */
  private void deleteFuncionario(Funcionario funcionario) {
    if (funcionario == null)
      return;

    try {
      if (jogoDao != null && jogoDao.existePorFuncionario(funcionario.getId())) {
        boolean inativar = showBlockedDialog(
            "Funcionário com jogos",
            "O funcionário '" + funcionario.getNome()
                + "' possui jogos registrados e não pode ser excluído. Deseja inativá-lo?");
        if (inativar) {
          funcionario.setAtivo(false);
          dao.editar(funcionario);
          refreshTable();
        }
        return;
      }
      dao.deletar(funcionario.getId());
      refreshTable();
    } catch (SQLException e) {
      System.err.println("Erro ao verificar jogos do funcionário: " + e.getMessage());
    } catch (Exception e) {
      System.err.println("Erro ao deletar funcionario: " + e.getMessage());
    }
  }

  private boolean showBlockedDialog(String titulo, String mensagem) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(titulo);
    alert.setHeaderText(titulo);
    alert.setContentText(mensagem);
    ButtonType btnNao = new ButtonType("Não", ButtonType.CANCEL.getButtonData());
    ButtonType btnSim = new ButtonType("Sim", ButtonType.OK.getButtonData());
    alert.getButtonTypes().setAll(btnNao, btnSim);
    javafx.scene.control.Button simButton = (javafx.scene.control.Button) alert.getDialogPane().lookupButton(btnSim);
    if (simButton != null) {
      simButton.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white;");
    }
    javafx.scene.control.Button naoButton = (javafx.scene.control.Button) alert.getDialogPane().lookupButton(btnNao);
    if (naoButton != null) {
      naoButton.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white;");
    }
    Optional<ButtonType> result = alert.showAndWait();
    return result.isPresent() && result.get() == btnSim;
  }
}
