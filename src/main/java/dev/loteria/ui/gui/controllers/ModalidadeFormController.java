package dev.loteria.ui.gui.controllers;

import dev.loteria.dao.ModalidadeDao;
import dev.loteria.models.Modalidade;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class ModalidadeFormController {

  @FXML
  private Label titleLabel;

  @FXML
  private TextField nomeField;

  @FXML
  private TextField numerosField;

  @FXML
  private TextField menorField;

  @FXML
  private TextField maiorField;

  @FXML
  private TextField valorField;

  @FXML
  private TextArea descricaoArea;

  @FXML
  private Button btnCancel;

  @FXML
  private Button btnSave;

  private Modalidade modalidade;
  private ModalidadeDao dao = new ModalidadeDao();

  // Callback invoked after save with the saved modalidade (may have generated id)
  private Consumer<Modalidade> onSaved;

  public void initialize() {
    btnCancel.setOnAction(e -> closeWindow());
    btnSave.setOnAction(e -> save());
  }

  public void setModalidade(Modalidade m) {
    this.modalidade = m;
    if (m != null) {
      titleLabel.setText("Editar Modalidade");
      nomeField.setText(m.getNome());
      numerosField.setText(String.valueOf(m.getNumerosSorteio()));
      menorField.setText(String.valueOf(m.getMenorBola()));
      maiorField.setText(String.valueOf(m.getMaiorBola()));
      valorField.setText(String.valueOf(m.getValorJogo()));
      descricaoArea.setText(m.getDescricao());
    } else {
      titleLabel.setText("Nova Modalidade");
    }
  }

  public void setOnSaved(Consumer<Modalidade> onSaved) {
    this.onSaved = onSaved;
  }

  private void save() {
    String nome = nomeField.getText();
    String numerosTxt = numerosField.getText();
    String menorTxt = menorField.getText();
    String maiorTxt = maiorField.getText();
    String valorTxt = valorField.getText();
    String descricao = descricaoArea.getText();

    if (nome == null || nome.isBlank()) {
      titleLabel.setText("Nome é obrigatório");
      return;
    }

    try {
      int numeros = Integer.parseInt(numerosTxt);
      int menor = Integer.parseInt(menorTxt);
      int maior = Integer.parseInt(maiorTxt);
      double valor = Double.parseDouble(valorTxt);

      if (modalidade == null) {
        modalidade = new Modalidade(nome, numeros, menor, maior, valor, descricao == null ? "" : descricao);
        dao.inserir(modalidade);
      } else {
        modalidade.setNome(nome);
        modalidade.setNumerosSorteio(numeros);
        modalidade.setMenorBola(menor);
        modalidade.setMaiorBola(maior);
        modalidade.setValorJogo(valor);
        modalidade.setDescricao(descricao == null ? "" : descricao);
        dao.editar(modalidade);
      }

      if (onSaved != null)
        onSaved.accept(modalidade);
      closeWindow();

    } catch (NumberFormatException ex) {
      titleLabel.setText("Valores numéricos inválidos");
    } catch (Exception ex) {
      titleLabel.setText("Erro ao salvar modalidade");
    }
  }

  private void closeWindow() {
    Stage stage = (Stage) btnCancel.getScene().getWindow();
    stage.close();
  }
}
