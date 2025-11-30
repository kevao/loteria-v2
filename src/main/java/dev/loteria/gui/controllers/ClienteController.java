package dev.loteria.gui.controllers;

import dev.loteria.dao.ClienteDao;
import dev.loteria.models.Cliente;
// import javafx.beans.property.SimpleStringProperty; (unused)
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Controller para a tela de listagem de Clientes.
 * Permite visualizar, criar, editar e deletar clientes.
 */
public class ClienteController {

  @FXML
  private TableView<Cliente> tableView;

  @FXML
  private TableColumn<Cliente, String> colNome;

  @FXML
  private TableColumn<Cliente, String> colEmail;

  @FXML
  private TableColumn<Cliente, String> colTelefone;

  @FXML
  private TableColumn<Cliente, String> colCpf;

  @FXML
  private TableColumn<Cliente, String> colEndereco;

  @FXML
  private TableColumn<Cliente, Boolean> colAtivo;

  @FXML
  private TableColumn<Cliente, Void> colActions;

  @FXML
  private Button btnNovo;

  private ClienteDao dao = new ClienteDao();

  /**
   * Inicializa o controller e configura a tabela.
   */
  @FXML
  public void initialize() {
    // Configurar colunas
    colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
    colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
    colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
    colEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));

    // Formatar coluna Ativo
    colAtivo.setCellValueFactory(new PropertyValueFactory<>("ativo"));
    colAtivo.setCellFactory(col -> new TableCell<Cliente, Boolean>() {
      @Override
      protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
        } else {
          setText(item ? "Sim" : "Não");
        }
      }
    });

    // Configurar coluna de ações com botões de editar e deletar
    colActions.setCellFactory(col -> new TableCell<Cliente, Void>() {
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
        btnEdit.getStyleClass().addAll("btn-icon", "btn-edit");
        btnEdit.setOnAction(e -> {
          Cliente cliente = getTableView().getItems().get(getIndex());
          openForm(cliente);
        });

        // Ícone de deletar (lixeira)
        SVGPath deleteIcon = new SVGPath();
        deleteIcon.setContent(
            "M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z");
        deleteIcon.getStyleClass().add("icon-delete");
        btnDelete.setGraphic(deleteIcon);
        btnDelete.getStyleClass().addAll("btn-icon", "btn-delete");
        btnDelete.setOnAction(e -> {
          Cliente cliente = getTableView().getItems().get(getIndex());
          deleteCliente(cliente);
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

    // Botão para novo cliente
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
      List<Cliente> clientes = new ArrayList<>();
      while (rs.next()) {
        Cliente c = new Cliente(
            rs.getObject("id", java.util.UUID.class),
            rs.getString("nome"),
            rs.getString("email"),
            rs.getString("telefone"),
            rs.getString("endereco"),
            rs.getString("cpf"),
            rs.getBoolean("ativo"));
        clientes.add(c);
      }
      try {
        rs.close();
      } catch (SQLException ignore) {
      }
      tableView.getItems().setAll(clientes);
    } catch (Exception e) {
      System.err.println("Erro ao carregar clientes: " + e.getMessage());
    }
  }

  /**
   * Abre o formulário de criação ou edição de cliente.
   *
   * @param cliente o cliente a ser editado, ou null para criar um novo
   */
  private void openForm(Cliente cliente) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/cliente_form.fxml"));
      Scene scene = new Scene(loader.load());
      // Use existing theme from resources
      scene.getStylesheets().add(getClass().getResource("/css/dark-theme.css").toExternalForm());

      ClienteFormController controller = loader.getController();
      controller.setCliente(cliente);
      controller.setOnSaved(c -> refreshTable());

      Stage stage = new Stage();
      stage.setTitle(cliente == null ? "Novo Cliente" : "Editar Cliente");
      stage.setScene(scene);
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.showAndWait();
    } catch (IOException e) {
      System.err.println("Erro ao abrir formulário de cliente: " + e.getMessage());
    }
  }

  /**
   * Deleta um cliente após confirmação.
   *
   * @param cliente o cliente a ser deletado
   */
  private void deleteCliente(Cliente cliente) {
    try {
      dao.deletar(cliente.getId());
      refreshTable();
    } catch (Exception e) {
      System.err.println("Erro ao deletar cliente: " + e.getMessage());
    }
  }
}
