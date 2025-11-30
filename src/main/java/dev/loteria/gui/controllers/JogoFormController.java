package dev.loteria.gui.controllers;

import dev.loteria.dao.JogoDao;
import dev.loteria.dao.ModalidadeDao;
import dev.loteria.dao.ClienteDao;
import dev.loteria.dao.FuncionarioDao;
import dev.loteria.models.Jogo;
import dev.loteria.models.Modalidade;
import dev.loteria.models.Cliente;
import dev.loteria.models.Funcionario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Formulário para criação/edição de Jogo.
 */
public class JogoFormController {

  @FXML
  private Label titleLabel;

  @FXML
  private Label lblInstrucaoNumeros;

  @FXML
  private ComboBox<Modalidade> modalidadeCombo;

  @FXML
  private ComboBox<Cliente> clienteCombo;

  @FXML
  private ComboBox<Funcionario> funcionarioCombo;

  @FXML
  private FlowPane numerosFlowPane;

  @FXML
  private Button btnCancel;

  @FXML
  private Button btnSave;

  private Jogo jogo;
  private JogoDao dao;
  private ModalidadeDao mDao = new ModalidadeDao();
  private ClienteDao cDao = new ClienteDao();
  private FuncionarioDao fDao = new FuncionarioDao();
  private Consumer<Jogo> onSaved;

  public JogoFormController() {
    try {
      dao = new JogoDao();
    } catch (SQLException e) {
      dao = null;
    }
  }

  @FXML
  public void initialize() {
    btnCancel.setOnAction(e -> closeWindow());
    btnSave.setOnAction(e -> save());
    
    setupConverters();

    modalidadeCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
        generateNumberGrid(newVal);
    });
    
    loadCombos();
  }

  private void setupConverters() {
      modalidadeCombo.setConverter(new StringConverter<Modalidade>() {
          @Override
          public String toString(Modalidade object) {
              return object == null ? null : object.getNome();
          }
          @Override
          public Modalidade fromString(String string) {
              return null;
          }
      });

      clienteCombo.setConverter(new StringConverter<Cliente>() {
          @Override
          public String toString(Cliente object) {
              return object == null ? null : object.getNome();
          }
          @Override
          public Cliente fromString(String string) {
              return null;
          }
      });

      funcionarioCombo.setConverter(new StringConverter<Funcionario>() {
          @Override
          public String toString(Funcionario object) {
              return object == null ? null : object.getNome();
          }
          @Override
          public Funcionario fromString(String string) {
              return null;
          }
      });
  }

  private void generateNumberGrid(Modalidade modalidade) {
      numerosFlowPane.getChildren().clear();
      
      if (modalidade == null) {
          lblInstrucaoNumeros.setText("Selecione uma modalidade para ver os números");
          return;
      }

      lblInstrucaoNumeros.setText("Escolha " + modalidade.getNumerosSorteio() + " números");

      int min = modalidade.getMenorBola();
      int max = modalidade.getMaiorBola();
      
      // Se já houver um jogo carregado e a modalidade for a mesma, recupera os números selecionados
      Set<Integer> selectedNumbers = new LinkedHashSet<>();
      if (jogo != null && jogo.getModalidade() != null && jogo.getModalidade().getId().equals(modalidade.getId())) {
          selectedNumbers.addAll(jogo.getNumeros());
      }

      for (int i = min; i <= max; i++) {
          ToggleButton btn = new ToggleButton(String.valueOf(i));
          btn.setPrefWidth(40);
          btn.setPrefHeight(40);
          final int num = i;
          
          if (selectedNumbers.contains(num)) {
              btn.setSelected(true);
          }
          
          // Lógica de seleção (opcional: limitar quantidade de números selecionados)
          // Por enquanto, livre seleção
          
          numerosFlowPane.getChildren().add(btn);
      }
  }

  private void loadCombos() {
    try {
      ResultSet mrs = mDao.listar();
      java.util.List<Modalidade> ms = new java.util.ArrayList<>();
      while (mrs != null && mrs.next()) {
        Modalidade modalidade = mDao.getById(mrs.getObject("id", java.util.UUID.class));
        if (modalidade != null && modalidade.isAtivo()) {
          ms.add(modalidade);
        }
      }
      if (mrs != null)
        try {
          mrs.close();
        } catch (SQLException ignore) {
        }
      if (jogo != null && jogo.getModalidade() != null) {
        java.util.UUID selectedId = jogo.getModalidade().getId();
        boolean contains = ms.stream().anyMatch(existing -> {
          if (existing == null)
            return false;
          if (selectedId == null || existing.getId() == null)
            return existing == jogo.getModalidade();
          return selectedId.equals(existing.getId());
        });
        if (!contains) {
          ms.add(jogo.getModalidade());
        }
      }
      modalidadeCombo.setItems(FXCollections.observableArrayList(ms));

      ResultSet crs = cDao.listar();
      java.util.List<Cliente> cs = new java.util.ArrayList<>();
      while (crs != null && crs.next()) {
        Cliente cliente = cDao.getById(crs.getObject("id", java.util.UUID.class));
        if (cliente != null && cliente.isAtivo()) {
          cs.add(cliente);
        }
      }
      if (crs != null)
        try {
          crs.close();
        } catch (SQLException ignore) {
        }
      if (jogo != null && jogo.getCliente() != null) {
        java.util.UUID clienteId = jogo.getCliente().getId();
        boolean containsCliente = cs.stream().anyMatch(existing -> {
          if (existing == null)
            return false;
          if (clienteId == null || existing.getId() == null)
            return existing == jogo.getCliente();
          return clienteId.equals(existing.getId());
        });
        if (!containsCliente) {
          cs.add(jogo.getCliente());
        }
      }
      clienteCombo.setItems(FXCollections.observableArrayList(cs));

      ResultSet frs = fDao.listar();
      java.util.List<Funcionario> fs = new java.util.ArrayList<>();
      while (frs != null && frs.next()) {
        Funcionario funcionario = fDao.getById(frs.getObject("id", java.util.UUID.class));
        if (funcionario != null && funcionario.isAtivo()) {
          fs.add(funcionario);
        }
      }
      if (frs != null)
        try {
          frs.close();
        } catch (SQLException ignore) {
        }
      if (jogo != null && jogo.getFuncionario() != null) {
        java.util.UUID funcionarioId = jogo.getFuncionario().getId();
        boolean containsFuncionario = fs.stream().anyMatch(existing -> {
          if (existing == null)
            return false;
          if (funcionarioId == null || existing.getId() == null)
            return existing == jogo.getFuncionario();
          return funcionarioId.equals(existing.getId());
        });
        if (!containsFuncionario) {
          fs.add(jogo.getFuncionario());
        }
      }
      funcionarioCombo.setItems(FXCollections.observableArrayList(fs));
    } catch (Exception e) {
      System.err.println("Erro ao carregar combos: " + e.getMessage());
    }
  }

  public void setJogo(Jogo j) {
    this.jogo = j;
    if (j != null) {
      titleLabel.setText("Editar Jogo");
      modalidadeCombo.getSelectionModel().select(j.getModalidade());
      clienteCombo.getSelectionModel().select(j.getCliente());
      funcionarioCombo.getSelectionModel().select(j.getFuncionario());
      // A grid será gerada pelo listener do combo box
    } else {
      titleLabel.setText("Novo Jogo");
      lblInstrucaoNumeros.setText("Selecione uma modalidade para ver os números");
      numerosFlowPane.getChildren().clear();
    }
  }

  public void setOnSaved(Consumer<Jogo> onSaved) {
    this.onSaved = onSaved;
  }

  private void save() {
    Modalidade m = modalidadeCombo.getValue();
    Cliente c = clienteCombo.getValue();
    Funcionario f = funcionarioCombo.getValue();
    
    if (m == null || c == null || f == null) {
      titleLabel.setText("Modalidade/Cliente/Funcionário obrigatórios");
      return;
    }
    
    Set<Integer> set = new LinkedHashSet<>();
    numerosFlowPane.getChildren().forEach(node -> {
        if (node instanceof ToggleButton) {
            ToggleButton btn = (ToggleButton) node;
            if (btn.isSelected()) {
                try {
                    set.add(Integer.parseInt(btn.getText()));
                } catch (NumberFormatException ex) {}
            }
        }
    });

    try {
      if (jogo == null) {
        jogo = new Jogo(m, c, f, set);
        if (dao != null)
          dao.inserir(jogo);
      } else {
        jogo.setModalidade(m);
        jogo.setCliente(c);
        jogo.setFuncionario(f);
        jogo.setNumeros(set);
        // no editar implementado no DAO
      }
      if (onSaved != null)
        onSaved.accept(jogo);
      closeWindow();
    } catch (SQLException e) {
      titleLabel.setText("Erro ao salvar jogo: " + e.getMessage());
    }
  }

  private void closeWindow() {
    Stage stage = (Stage) btnCancel.getScene().getWindow();
    stage.close();
  }
}
