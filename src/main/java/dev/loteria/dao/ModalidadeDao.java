package dev.loteria.dao;

import dev.loteria.database.Conexao;
import java.sql.Connection;
import dev.loteria.interfaces.CRUD;
import dev.loteria.models.Modalidade;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO responsável por operações de persistência para
 * {@link dev.loteria.models.Modalidade}. Fornece métodos para inserir,
 * editar, listar e recuperar modalidades.
 */
public class ModalidadeDao implements CRUD<Modalidade> {

  private Connection conn;
  private PreparedStatement ps;

  public ModalidadeDao() {
    conn = Conexao.getConn();
  }

  /**
   * Constroi o DAO de Modalidade utilizando a conexão compartilhada.
   */

  /**
   * Retorna todas as modalidades cadastradas como um {@link ResultSet}.
   *
   * @return {@link ResultSet} com todas as modalidades ou `null` em caso de
   *         erro
   */
  public ResultSet listar() {
    try {
      return conn.createStatement().executeQuery("SELECT * FROM modalidades");
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao listar modalidades.");
    }

    return null;
  }

  /**
   * Método placeholder para compatibilidade com APIs que usam
   * auto-increment. Não é necessário para chaves UUID.
   */
  public void resetarAutoIncrement() {
    // not needed for UUID primary keys
  }

  /**
   * Insere uma nova {@link Modalidade} na base de dados. Após a inserção, o
   * campo `id` do objeto será preenchido com o UUID retornado pelo banco.
   *
   * @param modalidade instância a ser persistida
   */
  public void inserir(Modalidade modalidade) {
    try {
      String sql = "INSERT INTO modalidades (nome, numeros_sorteio, menor_bola, maior_bola, valor_jogo, descricao, ativo) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";

      ps = conn.prepareStatement(sql);

      ps.setString(1, modalidade.getNome());
      ps.setInt(2, modalidade.getNumerosSorteio());
      ps.setInt(3, modalidade.getMenorBola());
      ps.setInt(4, modalidade.getMaiorBola());
      ps.setDouble(5, modalidade.getValorJogo());
      ps.setString(6, modalidade.getDescricao());
      ps.setBoolean(7, modalidade.isAtivo());

      ResultSet rs = ps.executeQuery();
      if (rs != null && rs.next()) {
        java.util.UUID id = rs.getObject("id", java.util.UUID.class);
        modalidade.setId(id);
      }
      if (rs != null)
        rs.close();

      ps.close();
      System.out.println("Modalidade inserida com sucesso!");
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao inserir a modalidade: " + e.getMessage());
    }
  }

  /**
   * Atualiza os dados de uma {@link Modalidade} existente. Se o ID não for
   * encontrado a operação é abortada.
   *
   * @param modalidade instância com ID e novos valores
   */
  public void editar(Modalidade modalidade) {

    if (!checkId(modalidade.getId())) {
      System.out.println("Modalidade com ID " + modalidade.getId() + " não existe.");
      return;
    }

    try {
      String sql = "UPDATE modalidades SET nome = ?, numeros_sorteio = ?, menor_bola = ?, maior_bola = ?, valor_jogo = ?, descricao = ?, ativo = ? WHERE id = ?";

      ps = conn.prepareStatement(sql);

      ps.setString(1, modalidade.getNome());
      ps.setInt(2, modalidade.getNumerosSorteio());
      ps.setInt(3, modalidade.getMenorBola());
      ps.setInt(4, modalidade.getMaiorBola());
      ps.setDouble(5, modalidade.getValorJogo());
      ps.setString(6, modalidade.getDescricao());
      ps.setBoolean(7, modalidade.isAtivo());
      ps.setObject(8, modalidade.getId());

      ps.executeUpdate();

      ps.close();
      System.out.println("Modalidade atualizada com sucesso!");
    } catch (SQLException ex) {
      System.out.println("Ocorreu um erro ao editar a modalidade.");
    }
  }

  /**
   * Remove a {@link Modalidade} identificada por `id` da base.
   *
   * @param id UUID da modalidade a ser removida
   */
  public void deletar(java.util.UUID id) {

    if (!checkId(id)) {
      System.out.println("Modalidade com ID " + id + " não existe.");
      return;
    }

    try {
      String sql = "DELETE FROM modalidades WHERE id = ?";
      ps = conn.prepareStatement(sql);
      ps.setObject(1, id);
      ps.executeUpdate();
      ps.close();
      System.out.println("Modalidade deletada com sucesso!");
    } catch (SQLException ex) {
      System.out.println("Ocorreu um erro ao deletar a modalidade.");
    }
  }

  /**
   * Retorna a contagem de registros na tabela `modalidades`.
   *
   * @return número total de modalidades (0 em caso de erro)
   */
  public int contar() {
    int count = 0;
    try {
      String sql = "SELECT COUNT(*) FROM modalidades";
      ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        count = rs.getInt(1);
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao contar as modalidades.");
    }
    return count;
  }

  /**
   * Verifica a existência de uma {@link Modalidade} pelo seu UUID.
   *
   * @param id UUID a ser verificado
   * @return `true` se existir, `false` caso contrário
   */
  public boolean checkId(java.util.UUID id) {
    try {
      String sql = "SELECT 1 FROM modalidades WHERE id = ?";
      ps = conn.prepareStatement(sql);
      ps.setObject(1, id);
      ResultSet rs = ps.executeQuery();
      boolean existe = rs.next();
      rs.close();
      ps.close();
      return existe;
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao verificar o ID da modalidade.");
    }
    return false;
  }

  /**
   * Recupera uma {@link Modalidade} pelo seu UUID.
   *
   * @param id UUID da modalidade
   * @return {@link Modalidade} encontrada ou `null` se não existir/ocorrer erro
   */
  public Modalidade getById(java.util.UUID id) {
    if (!checkId(id)) {
      System.out.println("Modalidade com ID " + id + " não existe.");
      return null;
    }

    try {
      String sql = "SELECT * FROM modalidades WHERE id = ?";
      ps = conn.prepareStatement(sql);
      ps.setObject(1, id);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        Modalidade modalidade = new Modalidade(
            rs.getObject("id", java.util.UUID.class),
            rs.getString("nome"),
            rs.getInt("numeros_sorteio"),
            rs.getInt("menor_bola"),
            rs.getInt("maior_bola"),
            rs.getDouble("valor_jogo"),
            rs.getString("descricao"),
            rs.getBoolean("ativo"));
        rs.close();
        ps.close();
        return modalidade;
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao buscar a modalidade pelo ID.");
    }
    return null;
  }
}
