package dev.loteria.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

  private static Conexao getInstance() {
    return INSTANCE;
  }

  public static Connection getConn() {
    return getInstance().conn;
  }

}
