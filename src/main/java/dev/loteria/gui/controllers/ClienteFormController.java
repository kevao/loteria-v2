package dev.loteria.gui.controllers;

import dev.loteria.dao.ClienteDao;
import dev.loteria.models.Cliente;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.stage.Stage;

import java.util.function.Consumer;

/**
 * Controller para o formulário de criação e edição de Cliente.
 */
public class ClienteFormController {

  @FXML
  private Label titleLabel;

  @FXML
  private TextField nomeField;

  @FXML
  private TextField emailField;

  @FXML
  private TextField telefoneField;

  @FXML
  private TextField cpfField;

  @FXML
  private TextField enderecoField;

  @FXML
  private CheckBox ativoCheck;

  @FXML
  private Button btnCancel;

  @FXML
  private Button btnSave;

  private Cliente cliente;
  private ClienteDao dao = new ClienteDao();
  private Consumer<Cliente> onSaved;

  /**
   * Inicializa o controller e configura os event handlers.
   */
  @FXML
  public void initialize() {
    btnCancel.setOnAction(e -> closeWindow());
    btnSave.setOnAction(e -> save());

    // Flags para evitar reentrância durante a formatação
    final AtomicBoolean cpfChanging = new AtomicBoolean(false);
    final AtomicBoolean telChanging = new AtomicBoolean(false);

    // Máscara CPF (segura com Platform.runLater e flag)
    cpfField.textProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal == null || newVal.equals(oldVal) || cpfChanging.get())
        return;
      String digits = newVal.replaceAll("[^0-9]", "");
      if (digits.length() > 11)
        digits = digits.substring(0, 11);

      String formatted;
      if (digits.length() <= 3) {
        formatted = digits;
      } else if (digits.length() <= 6) {
        formatted = digits.substring(0, 3) + "." + digits.substring(3);
      } else if (digits.length() <= 9) {
        formatted = digits.substring(0, 3) + "." + digits.substring(3, 6) + "." + digits.substring(6);
      } else {
        formatted = digits.substring(0, 3) + "." + digits.substring(3, 6) + "." + digits.substring(6, 9) + "-"
            + digits.substring(9);
      }

      if (!newVal.equals(formatted)) {
        final int caret = cpfField.getCaretPosition();
        cpfChanging.set(true);
        Platform.runLater(() -> {
          cpfField.setText(formatted);
          int pos = Math.max(0, Math.min(formatted.length(), caret));
          cpfField.positionCaret(pos);
          cpfChanging.set(false);
        });
      }
    });

    // Máscara Telefone (segura com Platform.runLater e flag)
    telefoneField.textProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal == null || newVal.equals(oldVal) || telChanging.get())
        return;
      String digits = newVal.replaceAll("[^0-9]", "");
      if (digits.length() > 11)
        digits = digits.substring(0, 11);

      String formatted;
      if (digits.length() == 0) {
        formatted = "";
      } else if (digits.length() <= 2) {
        formatted = "(" + digits;
      } else if (digits.length() <= 6) {
        formatted = "(" + digits.substring(0, 2) + ") " + digits.substring(2);
      } else if (digits.length() <= 10) {
        formatted = "(" + digits.substring(0, 2) + ") " + digits.substring(2, 6) + "-" + digits.substring(6);
      } else {
        formatted = "(" + digits.substring(0, 2) + ") " + digits.substring(2, 7) + "-" + digits.substring(7);
      }

      if (!newVal.equals(formatted)) {
        final int caret = telefoneField.getCaretPosition();
        telChanging.set(true);
        Platform.runLater(() -> {
          telefoneField.setText(formatted);
          int pos = Math.max(0, Math.min(formatted.length(), caret));
          telefoneField.positionCaret(pos);
          telChanging.set(false);
        });
      }
    });
  }

  /**
   * Define o cliente a ser editado. Se null, será criado um novo.
   *
   * @param c o cliente para edição, ou null para criar novo
   */
  public void setCliente(Cliente c) {
    this.cliente = c;
    if (c != null) {
      titleLabel.setText("Editar Cliente");
      nomeField.setText(c.getNome());
      emailField.setText(c.getEmail());
      telefoneField.setText(c.getTelefone());
      cpfField.setText(c.getCpf());
      enderecoField.setText(c.getEndereco());
      ativoCheck.setSelected(c.isAtivo());
    } else {
      titleLabel.setText("Novo Cliente");
      ativoCheck.setSelected(true); // Cliente ativo por padrão
    }
  }

  /**
   * Define o callback a ser executado após salvar o cliente.
   *
   * @param onSaved callback que recebe o cliente salvo
   */
  public void setOnSaved(Consumer<Cliente> onSaved) {
    this.onSaved = onSaved;
  }

  /**
   * Valida e salva o cliente no banco de dados.
   */
  private void save() {
    String nome = nomeField.getText();
    String email = emailField.getText();
    String telefone = telefoneField.getText();
    String cpf = cpfField.getText();
    String endereco = enderecoField.getText();
    boolean ativo = ativoCheck.isSelected();

    // Validações básicas
    if (nome == null || nome.isBlank()) {
      titleLabel.setText("Nome é obrigatório");
      return;
    }

    if (email == null || email.isBlank()) {
      titleLabel.setText("E-mail é obrigatório");
      return;
    }

    if (cpf == null || cpf.isBlank()) {
      titleLabel.setText("CPF é obrigatório");
      return;
    }

    try {
      if (cliente == null) {
        // Criar novo cliente
        cliente = new Cliente(nome, email, telefone == null ? "" : telefone,
            cpf, endereco == null ? "" : endereco, ativo);
        dao.inserir(cliente);
      } else {
        // Editar cliente existente
        cliente.setNome(nome);
        cliente.setEmail(email);
        cliente.setTelefone(telefone == null ? "" : telefone);
        cliente.setCpf(cpf);
        cliente.setEndereco(endereco == null ? "" : endereco);
        cliente.setAtivo(ativo);
        dao.editar(cliente);
      }

      if (onSaved != null) {
        onSaved.accept(cliente);
      }
      closeWindow();

    } catch (Exception ex) {
      titleLabel.setText("Erro ao salvar cliente: " + ex.getMessage());
    }
  }

  /**
   * Fecha a janela do formulário.
   */
  private void closeWindow() {
    Stage stage = (Stage) btnCancel.getScene().getWindow();
    stage.close();
  }
}
