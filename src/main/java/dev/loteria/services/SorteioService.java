package dev.loteria.services;

import dev.loteria.dao.ModalidadeDao;
import dev.loteria.dao.SorteioDao;
import dev.loteria.models.Modalidade;
import dev.loteria.models.Sorteio;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável por operações de negócio relacionadas a sorteios
 * (criação, listagem e validações relacionadas aos resultados).
 * Métodos puros sem lógica de apresentação.
 */
public class SorteioService {

  private final SorteioDao sorteioDao;
  private final ModalidadeDao modalidadeDao;

  /**
   * Construtor padrão que inicializa os DAOs.
   */
  public SorteioService() {
    this.sorteioDao = new SorteioDao();
    this.modalidadeDao = new ModalidadeDao();
  }

  /**
   * Retorna todos os sorteios registrados como objetos {@link Sorteio}.
   * Para exibir na GUI será necessário mapear campos adicionais (ex.: nome
   * modalidade).
   *
   * @return lista de sorteios ou lista vazia em caso de erro
   */
  public List<SorteioDTO> listarTodos() {
    List<SorteioDTO> sorteios = new ArrayList<>();
    try {
      ResultSet rs = sorteioDao.listar();
      while (rs != null && rs.next()) {
        java.util.UUID id = rs.getObject("id", java.util.UUID.class);
        String nomeModalidade = rs.getString("nome");
        String numerosText = rs.getString("numeros_sorteados");
        Timestamp ts = rs.getTimestamp("horario");
        LocalDateTime horario = ts != null ? ts.toLocalDateTime() : null;

        List<Integer> numeros = Arrays.stream(numerosText.split("-"))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(Integer::parseInt)
            .collect(Collectors.toList());

        SorteioDTO dto = new SorteioDTO(id, nomeModalidade, numeros, horario);
        sorteios.add(dto);
      }
      if (rs != null)
        rs.close();
    } catch (SQLException e) {
      System.err.println("Erro ao listar sorteios: " + e.getMessage());
    }
    return sorteios;
  }

  /**
   * Cria e persiste um novo sorteio para a modalidade informada.
   *
   * @param modalidadeId UUID da modalidade
   * @return o {@link Sorteio} criado ou null se a modalidade não existir
   * @throws IllegalArgumentException se o ID for nulo
   */
  public Sorteio inserir(java.util.UUID modalidadeId) {
    if (modalidadeId == null) {
      throw new IllegalArgumentException("ID da modalidade não pode ser nulo.");
    }
    Modalidade modalidade = modalidadeDao.getById(modalidadeId);
    if (modalidade == null) {
      throw new IllegalArgumentException("Modalidade não encontrada.");
    }
    Sorteio sorteio = new Sorteio(modalidade);
    sorteioDao.inserir(sorteio);
    return sorteio;
  }

  /**
   * Remove um sorteio pelo UUID.
   *
   * @param id UUID do sorteio
   * @throws IllegalArgumentException se o ID for nulo
   */
  public void deletar(java.util.UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("ID não pode ser nulo.");
    }
    sorteioDao.deletar(id);
  }

  /**
   * DTO para transferência de dados de sorteio para a camada de apresentação.
   */
  public static class SorteioDTO {
    private final java.util.UUID id;
    private final String nomeModalidade;
    private final List<Integer> numeros;
    private final LocalDateTime horario;

    public SorteioDTO(java.util.UUID id, String nomeModalidade, List<Integer> numeros, LocalDateTime horario) {
      this.id = id;
      this.nomeModalidade = nomeModalidade;
      this.numeros = numeros;
      this.horario = horario;
    }

    public java.util.UUID getId() {
      return id;
    }

    public String getNomeModalidade() {
      return nomeModalidade;
    }

    public List<Integer> getNumeros() {
      return numeros;
    }

    public LocalDateTime getHorario() {
      return horario;
    }
  }
}
