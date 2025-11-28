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
    criarTabela();
    criarMockups();
  }

  /**
   * Retorna um ResultSet com todas as modalidades cadastradas.
   * Se ocorrer um erro, exibe uma mensagem e retorna null.
   */
  public ResultSet listar() {
    try {
      return conexao.getConn().createStatement().executeQuery("SELECT * FROM modalidades");
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao listar modalidades.");
    }

    return null;
  }

  /**
   * Cria a tabela de modalidades no banco de dados se ela não existir.
   */
  public void criarTabela() {
    try {
      // ensure uuid extension available
      conexao.getConn().createStatement().executeUpdate("CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\";");

      String sql = """
          CREATE TABLE IF NOT EXISTS modalidades (
          id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
          nome VARCHAR(50) NOT NULL,
          numeros_sorteio INT NOT NULL,
          menor_bola INT NOT NULL,
          maior_bola INT NOT NULL,
          valor_jogo DOUBLE PRECISION NOT NULL,
          descricao VARCHAR(255) NOT NULL
          );
          """;

      conexao.getConn().createStatement().executeUpdate(sql);
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao criar a tabela de modalidades.");
    }
  }

  /*
   * Cria mockups de modalidades se a tabela estiver vazia.
   * Inicia com modalidades populares como Mega-Sena, Quina, Lotofácil e
   * Lotomania.
   * Reseta o AUTO_INCREMENT para garantir que os IDs comecem do 1.
   */
  public void criarMockups() {
    if (contar() != 0)
      return;
    System.out.println("Criando mockups de modalidades...");

    inserir(new Modalidade("Mega-Sena", 6, 1, 60, 6.0, "O jogo mais famoso do Brasil."));
    inserir(new Modalidade("Quina", 5, 1, 80, 4.5, "Jogo popular com sorteio de 5 números."));
    inserir(new Modalidade("Lotofácil", 15, 1, 25, 5.0, "Fácil de jogar, fácil de ganhar."));
    inserir(new Modalidade("Lotomania", 20, 0, 99, 2.5, "Jogo com 20 números sorteados entre 0 e 99."));
  }

  public void resetarAutoIncrement() {
    // not needed for UUID primary keys
  }

  /*
   * Insere uma nova modalidade na tabela.
   * Antes de inserir, reseta o AUTO_INCREMENT para garantir que o ID pegue sempre
   * o próximo número.
   */
  public void inserir(Modalidade modalidade) {
    try {
      String sql = "INSERT INTO modalidades (nome, numeros_sorteio, menor_bola, maior_bola, valor_jogo, descricao) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";

      ps = conexao.getConn().prepareStatement(sql);

      ps.setString(1, modalidade.getNome());
      ps.setInt(2, modalidade.getNumerosSorteio());
      ps.setInt(3, modalidade.getMenorBola());
      ps.setInt(4, modalidade.getMaiorBola());
      ps.setDouble(5, modalidade.getValorJogo());
      ps.setString(6, modalidade.getDescricao());

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

  /*
   * Edita uma modalidade existente.
   * Verifica se o ID existe antes de tentar editar.
   */
  public void editar(Modalidade modalidade) {

    if (!checkId(modalidade.getId())) {
      System.out.println("Modalidade com ID " + modalidade.getId() + " não existe.");
      return;
    }

    try {
      String sql = "UPDATE modalidades SET nome = ?, numeros_sorteio = ?, menor_bola = ?, maior_bola = ?, valor_jogo = ?, descricao = ? WHERE id = ?";

      ps = conexao.getConn().prepareStatement(sql);

      ps.setString(1, modalidade.getNome());
      ps.setInt(2, modalidade.getNumerosSorteio());
      ps.setInt(3, modalidade.getMenorBola());
      ps.setInt(4, modalidade.getMaiorBola());
      ps.setDouble(5, modalidade.getValorJogo());
      ps.setString(6, modalidade.getDescricao());
      ps.setObject(7, modalidade.getId());

      ps.executeUpdate();

      ps.close();
      System.out.println("Modalidade atualizada com sucesso!");
    } catch (SQLException ex) {
      System.out.println("Ocorreu um erro ao editar a modalidade.");
    }
  }

  /*
   * Deleta uma modalidade pelo ID.
   * Verifica se o ID existe antes de tentar deletar.
   * Se existir, deleta e atualiza os IDs das modalidades restantes.
   * Se não existir, exibe uma mensagem informando que a modalidade não foi
   * encontrada.
   */
  public void deletar(java.util.UUID id) {

    if (!checkId(id)) {
      System.out.println("Modalidade com ID " + id + " não existe.");
      return;
    }

    try {
      String sql = "DELETE FROM modalidades WHERE id = ?";
      ps = conexao.getConn().prepareStatement(sql);
      ps.setObject(1, id);
      ps.executeUpdate();
      ps.close();
      System.out.println("Modalidade deletada com sucesso!");
    } catch (SQLException ex) {
      System.out.println("Ocorreu um erro ao deletar a modalidade.");
    }
  }

  /*
   * Conta quantas modalidades existem na tabela.
   * Se ocorrer um erro, exibe uma mensagem e retorna 0.
   */
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

  /*
   * Verifica se uma modalidade com o ID fornecido existe.
   * Se não existir, exibe uma mensagem informando que a modalidade não foi
   * encontrada.
   * 
   * @param id ID da modalidade a ser verificado.
   * 
   * @return true se o ID existir, false caso contrário.
   * 
   */
  public boolean checkId(java.util.UUID id) {
    try {
      String sql = "SELECT 1 FROM modalidades WHERE id = ?";
      ps = conexao.getConn().prepareStatement(sql);
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
   * Busca uma modalidade pelo ID.
   * Se o ID não existir, exibe uma mensagem informando que a modalidade não foi
   * encontrada.
   */
  public Modalidade getById(java.util.UUID id) {
    if (!checkId(id)) {
      System.out.println("Modalidade com ID " + id + " não existe.");
      return null;
    }

    try {
      String sql = "SELECT * FROM modalidades WHERE id = ?";
      ps = conexao.getConn().prepareStatement(sql);
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
            rs.getString("descricao"));
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
