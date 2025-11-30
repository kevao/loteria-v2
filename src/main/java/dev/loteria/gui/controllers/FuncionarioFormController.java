package dev.loteria.gui.controllers;

import dev.loteria.dao.FuncionarioDao;
import dev.loteria.models.Funcionario;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.util.function.Consumer;

/**
 * Controller para o formulário de criação e edição de Funcionário.
 */
public class FuncionarioFormController {

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
  private TextField matriculaField;

  @FXML
  private TextField cargoField;

  @FXML
  private TextField salarioField;

  @FXML
  private TextField enderecoField;

  @FXML
  private CheckBox ativoCheck;

  @FXML
  private Button btnCancel;

  @FXML
  private Button btnSave;

  private Funcionario funcionario;
  private FuncionarioDao dao = new FuncionarioDao();
  private Consumer<Funcionario> onSaved;

  /**
   * Inicializa o controller e configura os event handlers.
   */
  @FXML
  public void initialize() {
    btnCancel.setOnAction(e -> closeWindow());
    btnSave.setOnAction(e -> save());

    // Máscara para campo de salário (digitação em centavos -> formata como R$
    // x.xxx,yy)
    final NumberFormat currency = NumberFormat.getCurrencyInstance(java.util.Locale.forLanguageTag("pt-BR"));
    salarioField.textProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
      private boolean changing = false;

      @Override
      public void changed(javafx.beans.value.ObservableValue<? extends String> obs, String oldVal, String newVal) {
        if (changing)
          return;
        changing = true;
        try {
          String digits = newVal == null ? "" : newVal.replaceAll("[^0-9]", "");
          if (digits.isEmpty()) {
            salarioField.setText("");
            return;
          }
          // interpret digits as cents
          long cents = Long.parseLong(digits);
          double value = cents / 100.0;
          salarioField.setText(currency.format(value));
        } catch (NumberFormatException ex) {
          // ignore invalid
        } finally {
          changing = false;
        }
      }
    });
  }

  /**
   * Define o funcionário a ser editado. Se null, será criado um novo.
   *
   * @param f o funcionário para edição, ou null para criar novo
   */
  public void setFuncionario(Funcionario f) {
    this.funcionario = f;
    if (f != null) {
      titleLabel.setText("Editar Funcionário");
      nomeField.setText(f.getNome());
      emailField.setText(f.getEmail());
      telefoneField.setText(f.getTelefone());
      cpfField.setText(f.getCpf());
      matriculaField.setText(f.getMatricula());
      cargoField.setText(f.getCargo());

      // Formatar salário como moeda pt-BR
      NumberFormat nf = NumberFormat.getCurrencyInstance(java.util.Locale.forLanguageTag("pt-BR"));
      salarioField.setText(nf.format(f.getSalario()));

      enderecoField.setText(f.getEndereco());
      ativoCheck.setSelected(f.isAtivo());
    } else {
      titleLabel.setText("Novo Funcionário");
      ativoCheck.setSelected(true); // Funcionário ativo por padrão
    }
  }

  /**
   * Define o callback a ser executado após salvar o funcionário.
   *
   * @param onSaved callback que recebe o funcionário salvo
   */
  public void setOnSaved(Consumer<Funcionario> onSaved) {
    this.onSaved = onSaved;
  }

  /**
   * Valida e salva o funcionário no banco de dados.
   */
  private void save() {
    String nome = nomeField.getText();
    String email = emailField.getText();
    String telefone = telefoneField.getText();
    String cpf = cpfField.getText();
    String matricula = matriculaField.getText();
    String cargo = cargoField.getText();
    String salarioTxt = salarioField.getText();
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

    if (matricula == null || matricula.isBlank()) {
      titleLabel.setText("Matrícula é obrigatória");
      return;
    }

    if (cargo == null || cargo.isBlank()) {
      titleLabel.setText("Cargo é obrigatório");
      return;
    }

    // Parse do salário formatado como R$ x.xxx,yy -> extract digits as cents
    double salario = 0.0;
    if (salarioTxt != null && !salarioTxt.isBlank()) {
      String digits = salarioTxt.replaceAll("[^0-9]", "");
      if (!digits.isEmpty()) {
        try {
          long cents = Long.parseLong(digits);
          salario = cents / 100.0;
        } catch (NumberFormatException ex) {
          titleLabel.setText("Salário inválido");
          return;
        }
      }
    }

    if (salario <= 0) {
      titleLabel.setText("Salário deve ser maior que zero");
      return;
    }

    try {
      if (funcionario == null) {
        // Criar novo funcionário
        funcionario = new Funcionario(nome, email, telefone == null ? "" : telefone,
            endereco == null ? "" : endereco, cpf, ativo,
            matricula, cargo, salario);
        dao.inserir(funcionario);
      } else {
        // Editar funcionário existente
        funcionario.setNome(nome);
        funcionario.setEmail(email);
        funcionario.setTelefone(telefone == null ? "" : telefone);
        funcionario.setCpf(cpf);
        funcionario.setMatricula(matricula);
        funcionario.setCargo(cargo);
        funcionario.setSalario(salario);
        funcionario.setEndereco(endereco == null ? "" : endereco);
        funcionario.setAtivo(ativo);
        dao.editar(funcionario);
      }

      if (onSaved != null) {
        onSaved.accept(funcionario);
      }
      closeWindow();

    } catch (Exception ex) {
      titleLabel.setText("Erro ao salvar funcionário: " + ex.getMessage());
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
