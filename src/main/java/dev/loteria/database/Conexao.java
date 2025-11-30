package dev.loteria.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe singleton responsável por gerenciar a conexão com o banco de dados
 * PostgreSQL.
 * 
 * @author Kevin Villanova
 */
public final class Conexao {

  private static final String URL = "jdbc:postgresql://localhost:5442/loteria";
  private static final String USUARIO = "root";
  private static final String SENHA = "rootpass";
  private static final Conexao INSTANCE = new Conexao();

  private final Connection conn;

  private Conexao() {
    try {
      conn = DriverManager.getConnection(URL, USUARIO, SENHA);
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao conectar ao banco de dados", e);
    }
  }

  /**
   * Retorna a instância singleton da classe.
   */
  private static Conexao getInstance() {
    return INSTANCE;
  }

  /**
   * Retorna a conexão montada com o banco de dados.
   * 
   * @return Conexão JDBC
   */
  public static Connection getConn() {
    return getInstance().conn;
  }

  /**
   * Fecha a conexão subjacente — atualmente não exposto publicamente.
   * Método reservado para uso futuro se necessário.
   */

}
