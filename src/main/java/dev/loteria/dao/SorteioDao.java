package dev.loteria.dao;

import dev.loteria.database.Conexao;
import dev.loteria.interfaces.CRUD;
import dev.loteria.models.Sorteio;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SorteioDao implements CRUD<Sorteio> {

  private Conexao conexao;
  private PreparedStatement ps;

  public SorteioDao() {
    conexao = new Conexao();
    criarTabela();
  }

  /**
   * Retorna um ResultSet com todos os sorteios cadastrados.
   */
  public ResultSet listar() {
    try {
      return conexao.getConn().createStatement().executeQuery(
          "SELECT s.id, m.nome, s.numeros_sorteados, s.horario FROM sorteios s INNER JOIN modalidades m ON s.modalidade_id = m.id");
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao listar sorteios.");
    }
    return null;
  }

  /**
   * Cria a tabela de sorteios no banco de dados se ela não existir.
   */
  public void criarTabela() {
    try {
      String sql = """
          CREATE TABLE IF NOT EXISTS sorteios (
          id INT AUTO_INCREMENT PRIMARY KEY,
          modalidade_id INT NOT NULL,
          numeros_sorteados TEXT NOT NULL,
          horario DATETIME NOT NULL,
          FOREIGN KEY (modalidade_id) REFERENCES modalidades(id)
          );
          """;
      conexao.getConn().createStatement().executeUpdate(sql);
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao criar a tabela de sorteios.");
    }
  }

  /*
   * Insere um novo sorteio na tabela.
   */
  public void inserir(Sorteio sorteio) {
    try {
      String sql = "INSERT INTO sorteios (modalidade_id, horario, numeros_sorteados) VALUES (?, ?, ?)";
      ps = conexao.getConn().prepareStatement(sql);
      ps.setInt(1, sorteio.getModalidade().getId());
      ps.setTimestamp(2, java.sql.Timestamp.valueOf(sorteio.getHorario()));
      ps.setString(3, String.join("-", sorteio.getNumerosSorteados().stream().map(String::valueOf).toList()));
      ps.executeUpdate();
      ps.close();
      System.out.println("Sorteio realizado com sucesso!");
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao realizar o sorteio");
    }
  }

  /*
   * Deleta um sorteio pelo ID.
   * Verifica se o ID existe antes de tentar deletar.
   * 
   * @param id ID do sorteio a ser deletado.
   * Se o ID não existir, exibe uma mensagem de erro.
   */
  public void deletar(int id) {
    if (!checkId(id)) {
      System.out.println("Sorteio com o ID informado não existe.");
      return;
    }

    try {
      String sql = "DELETE FROM sorteios WHERE id = ?";
      ps = conexao.getConn().prepareStatement(sql);
      ps.setInt(1, id);
      ps.executeUpdate();
      ps.close();
      System.out.println("Sorteio deletado com sucesso!");
    } catch (SQLException ex) {
      System.out.println("Ocorreu um erro ao deletar o sorteio.");
    }
  }

  /*
   * Conta quantos sorteios existem na tabela.
   * 
   * @return O número de sorteios cadastrados.
   */
  public int contar() {
    int count = 0;
    try {
      String sql = "SELECT COUNT(*) FROM sorteios";
      ps = conexao.getConn().prepareStatement(sql);
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
  public boolean checkId(int id) {
    try {
      String sql = "SELECT 1 FROM sorteios WHERE id = ?";
      ps = conexao.getConn().prepareStatement(sql);
      ps.setInt(1, id);
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

  public void editar(Sorteio sorteio) {
    System.out.println("Não é possível editar um sorteio.");
  }
}
