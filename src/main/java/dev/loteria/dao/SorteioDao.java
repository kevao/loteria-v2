package dev.loteria.dao;

import dev.loteria.database.Conexao;
import java.sql.Connection;
import dev.loteria.interfaces.CRUD;
import dev.loteria.models.Sorteio;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO para a entidade {@link dev.loteria.models.Sorteio}. Exponhe métodos
 * para listar sorteios, inserir resultados e consultar histórico.
 */
public class SorteioDao implements CRUD<Sorteio> {

  private Connection conn;
  private PreparedStatement ps;

  public SorteioDao() {
    conn = Conexao.getConn();
  }

  /**
   * Inicializa o DAO de sorteios usando a conexão compartilhada.
   */

  /**
   * Retorna um ResultSet com todos os sorteios cadastrados.
   */
  public ResultSet listar() {
    try {
      return conn.createStatement().executeQuery(
          "SELECT s.id, m.nome, s.numeros_sorteados, s.horario FROM sorteios s INNER JOIN modalidades m ON s.modalidade_id = m.id");
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao listar sorteios.");
    }
    return null;
  }

  /**
   * Persiste um novo {@link Sorteio} na base. Após inserção, o campo `id`
   * do objeto será preenchido com o UUID retornado pelo banco quando
   * disponível.
   *
   * @param sorteio instância de {@link Sorteio} com dados do sorteio
   */
  public void inserir(Sorteio sorteio) {
    try {
      String sql = "INSERT INTO sorteios (modalidade_id, horario, numeros_sorteados) VALUES (?, ?, ?) RETURNING id";
      ps = conn.prepareStatement(sql);
      ps.setObject(1, sorteio.getModalidade().getId());
      ps.setTimestamp(2, java.sql.Timestamp.valueOf(sorteio.getHorario()));
      ps.setString(3, String.join("-", sorteio.getNumerosSorteados().stream().map(String::valueOf).toList()));
      ResultSet rs = ps.executeQuery();
      if (rs != null && rs.next()) {
        java.util.UUID id = rs.getObject("id", java.util.UUID.class);
        sorteio.setId(id);
      }
      if (rs != null)
        rs.close();
      ps.close();
      System.out.println("Sorteio realizado com sucesso!");
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao realizar o sorteio");
    }
  }

  /**
   * Remove um {@link Sorteio} identificado por `id`.
   *
   * @param id UUID do sorteio a ser removido
   */
  public void deletar(java.util.UUID id) {
    if (!checkId(id)) {
      System.out.println("Sorteio com o ID informado não existe.");
      return;
    }

    try {
      String sql = "DELETE FROM sorteios WHERE id = ?";
      ps = conn.prepareStatement(sql);
      ps.setObject(1, id);
      ps.executeUpdate();
      ps.close();
      System.out.println("Sorteio deletado com sucesso!");
    } catch (SQLException ex) {
      System.out.println("Ocorreu um erro ao deletar o sorteio.");
    }
  }

  /**
   * Retorna o total de sorteios registrados na tabela `sorteios`.
   *
   * @return número total de sorteios (0 em caso de erro)
   */
  public int contar() {
    int count = 0;
    try {
      String sql = "SELECT COUNT(*) FROM sorteios";
      ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        count = rs.getInt(1);
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao contar os sorteios.");
    }
    return count;
  }

  /**
   * Checa se um sorteio com o ID informado existe na tabela.
   * 
   * @param id ID do sorteio a ser verificado.
   * @return true se o sorteio existir, false caso contrário.
   */
  public boolean checkId(java.util.UUID id) {
    try {
      String sql = "SELECT 1 FROM sorteios WHERE id = ?";
      ps = conn.prepareStatement(sql);
      ps.setObject(1, id);
      ResultSet rs = ps.executeQuery();
      boolean existe = rs.next();
      rs.close();
      ps.close();
      return existe;
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao verificar a existência do sorteio.");
      return false;
    }
  }

  /**
   * A edição de sorteios não é suportada — sorteios são registros irreversíveis
   * do histórico.
   *
   * @param sorteio instância a ser editada (não utilizada)
   */
  public void editar(Sorteio sorteio) {
    System.out.println("Não é possível editar um sorteio.");
  }
}
