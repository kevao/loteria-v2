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
import java.text.NumberFormat;

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

    // Validação: somente inteiros não-negativos de até 2 dígitos para campos de
    // bolas
    javafx.scene.control.TextFormatter<String> twoDigitFilter = new javafx.scene.control.TextFormatter<>(change -> {
      String newText = change.getControlNewText();
      if (newText.matches("\\d{0,2}"))
        return change;
      return null;
    });
    numerosField.setTextFormatter(twoDigitFilter);
    menorField.setTextFormatter(new javafx.scene.control.TextFormatter<>(change -> {
      String newText = change.getControlNewText();
      if (newText.matches("\\d{0,2}"))
        return change;
      return null;
    }));
    maiorField.setTextFormatter(new javafx.scene.control.TextFormatter<>(change -> {
      String newText = change.getControlNewText();
      if (newText.matches("\\d{0,2}"))
        return change;
      return null;
    }));

    // Mascara simples para campo de valor (digitação em centavos -> formata como R$
    // 1.234,56)
    final NumberFormat currency = NumberFormat.getCurrencyInstance(java.util.Locale.forLanguageTag("pt-BR"));
    valorField.textProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
      private boolean changing = false;

      @Override
      public void changed(javafx.beans.value.ObservableValue<? extends String> obs, String oldVal, String newVal) {
        if (changing)
          return;
        changing = true;
        try {
          String digits = newVal == null ? "" : newVal.replaceAll("[^0-9]", "");
          if (digits.isEmpty()) {
            valorField.setText("");
            return;
          }
          // interpret digits as cents
          long cents = Long.parseLong(digits);
          double value = cents / 100.0;
          valorField.setText(currency.format(value));
        } catch (NumberFormatException ex) {
          // ignore invalid
        } finally {
          changing = false;
        }
      }
    });
  }

  public void setModalidade(Modalidade m) {
    this.modalidade = m;
    if (m != null) {
      titleLabel.setText("Editar Modalidade");
      nomeField.setText(m.getNome());
      numerosField.setText(String.valueOf(m.getNumerosSorteio()));
      menorField.setText(String.valueOf(m.getMenorBola()));
      maiorField.setText(String.valueOf(m.getMaiorBola()));
      // formatar valor como moeda pt-BR
      NumberFormat nf = NumberFormat.getCurrencyInstance(java.util.Locale.forLanguageTag("pt-BR"));
      valorField.setText(nf.format(m.getValorJogo()));
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
      int numeros = Integer.parseInt(numerosTxt.isBlank() ? "0" : numerosTxt);
      int menor = Integer.parseInt(menorTxt.isBlank() ? "0" : menorTxt);
      int maior = Integer.parseInt(maiorTxt.isBlank() ? "0" : maiorTxt);

      // parse valorField formatted as R$ x.xxx,yy -> extract digits as cents
      double valor = 0.0;
      if (valorTxt != null && !valorTxt.isBlank()) {
        String digits = valorTxt.replaceAll("[^0-9]", "");
        if (!digits.isEmpty()) {
          long cents = Long.parseLong(digits);
          valor = cents / 100.0;
        }
      }

      // Validações adicionais:
      if (numeros < 2) {
        titleLabel.setText("Pelo menos duas bolas para sorteio");
        return;
      }

      if (menor < 0 || menor > 99 || maior < 0 || maior > 99) {
        titleLabel.setText("Valores de bolas devem ser entre 0 e 99");
        return;
      }

      if (menor >= maior) {
        titleLabel.setText("Número menor deve ser menor que o número maior");
        return;
      }

      int intervalo = (maior - menor + 1); // quantidade de números disponíveis no intervalo (inclusivo)
      int necessario = Math.max(5, numeros + 1);
      if (intervalo < necessario) {
        titleLabel.setText(String.format("Intervalo precisa ter pelo menos %d números", necessario));
        return;
      }

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
