package dev.loteria.services;

import dev.loteria.dao.ModalidadeDao;
import dev.loteria.models.Modalidade;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviço que orquestra regras de negócio relacionadas a {@link Modalidade}.
 * Métodos puros que delegam para o DAO, sem lógica de apresentação
 * (IO/console).
 * 
 * @author Kevin Villanova
 */
public class ModalidadeService {

  private final ModalidadeDao modalidadeDao;

  /**
   * Construtor padrão que inicializa o DAO.
   */
  public ModalidadeService() {
    this.modalidadeDao = new ModalidadeDao();
  }

  /**
   * Retorna uma lista de todas as modalidades cadastradas.
   *
   * @return lista de {@link Modalidade} ou lista vazia em caso de erro
   */
  public List<Modalidade> listarTodas() {
    List<Modalidade> modalidades = new ArrayList<>();
    try {
      ResultSet rs = modalidadeDao.listar();
      while (rs != null && rs.next()) {
        Modalidade m = new Modalidade(
            rs.getObject("id", java.util.UUID.class),
            rs.getString("nome"),
            rs.getInt("numeros_sorteio"),
            rs.getInt("menor_bola"),
            rs.getInt("maior_bola"),
            rs.getDouble("valor_jogo"),
            rs.getString("descricao"),
            rs.getBoolean("ativo"));
        modalidades.add(m);
      }
      if (rs != null)
        rs.close();
    } catch (SQLException e) {
      System.err.println("Erro ao listar modalidades: " + e.getMessage());
    }
    return modalidades;
  }

  /**
   * Insere uma nova modalidade no banco de dados.
   *
   * @param modalidade instância a ser persistida
   * @throws IllegalArgumentException se a modalidade for inválida
   */
  public void inserir(Modalidade modalidade) {
    validarModalidade(modalidade);
    modalidadeDao.inserir(modalidade);
  }

  /**
   * Atualiza uma modalidade existente.
   *
   * @param modalidade instância com ID e novos valores
   * @throws IllegalArgumentException se a modalidade for inválida ou ID nulo
   */
  public void editar(Modalidade modalidade) {
    if (modalidade.getId() == null) {
      throw new IllegalArgumentException("ID da modalidade não pode ser nulo.");
    }
    validarModalidade(modalidade);
    modalidadeDao.editar(modalidade);
  }

  /**
   * Remove uma modalidade pelo seu UUID.
   *
   * @param id UUID da modalidade a ser removida
   * @throws IllegalArgumentException se o ID for nulo
   */
  public void deletar(java.util.UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("ID não pode ser nulo.");
    }
    modalidadeDao.deletar(id);
  }

  /**
   * Busca uma modalidade pelo UUID.
   *
   * @param id UUID da modalidade
   * @return {@link Modalidade} ou null se não existir
   */
  public Modalidade buscarPorId(java.util.UUID id) {
    return modalidadeDao.getById(id);
  }

  /**
   * Valida os campos obrigatórios e regras de negócio de uma modalidade.
   *
   * @param modalidade instância a validar
   * @throws IllegalArgumentException se houver erro de validação
   */
  private void validarModalidade(Modalidade modalidade) {
    if (modalidade.getNome() == null || modalidade.getNome().trim().isEmpty()) {
      throw new IllegalArgumentException("Nome da modalidade é obrigatório.");
    }
    if (modalidade.getNumerosSorteio() < 1) {
      throw new IllegalArgumentException("Quantidade de números no sorteio deve ser >= 1.");
    }
    if (modalidade.getMenorBola() >= modalidade.getMaiorBola()) {
      throw new IllegalArgumentException("Menor bola deve ser menor que maior bola.");
    }
    if (modalidade.getValorJogo() <= 0) {
      throw new IllegalArgumentException("Valor do jogo deve ser positivo.");
    }
  }
}
