package dev.loteria.dao;

import dev.loteria.database.Conexao;
import dev.loteria.interfaces.CRUD;
import dev.loteria.models.Modalidade;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModalidadeDao implements CRUD<Modalidade> {

  private Conexao conexao;
  private PreparedStatement ps;

  public ModalidadeDao() {
    conexao = new Conexao();
  }

  public ResultSet listar() {
    try {
      return conexao.getConn().createStatement().executeQuery("SELECT * FROM modalidades");
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao listar modalidades.");
    }

    return null;
  }

  public void inserir(Modalidade modalidade) {
    try {
      String sql = "INSERT INTO modalidades VALUES (?, ?)";

      ps = conexao.getConn().prepareStatement(sql);

      ps.setString(1, modalidade.getNome());
      ps.setString(2, modalidade.getDescricao());

      ps.executeUpdate();

      ps.close();
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao inserir a modalidade");

    }

    System.out.println("Modalidade inserida com sucesso!");
  }

  public void deletar(int id) {
    try {
      String SQL = "DELETE FROM modalidades WHERE id = ?";

      ps = conexao.getConn().prepareStatement(SQL);

      ps.setInt(1, id);

      ps.executeUpdate();

      ps.close();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public void editar(Modalidade modalidade) {
    try {
      String SQL = "UPDATE modalidades SET nome = ?, descricao = ? WHERE id = ?";

      ps = conexao.getConn().prepareStatement(SQL);

      ps.setString(1, modalidade.getNome());
      ps.setString(2, modalidade.getDescricao());
      ps.setInt(3, modalidade.getId());

      ps.executeUpdate();

      ps.close();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public int contar() {
    int count = 0;
    try {
      String sql = "SELECT COUNT(*) FROM modalidades";
      ps = conexao.getConn().prepareStatement(sql);
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
}
