package dev.loteria.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
  private Connection conn;

  public Conexao() {
    try {
      // Conecta ao PostgreSQL na porta 5442 (host +10 conforme solicitado)
      String url = "jdbc:postgresql://localhost:5442/loteria";
      String usuario = "root";
      String senha = "rootpass";
      conn = DriverManager.getConnection(url, usuario, senha);
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao conectar ao banco de dados", e);
    }
  }

  public Connection getConn() {
    return conn;
  }

}
