package dev.loteria.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
  private Connection conn;

  public Conexao() {
    try {
      String url = "jdbc:mysql://localhost:3306/loteria";
      String usuario = "root";
      String senha = "";
      conn = DriverManager.getConnection(url, usuario, senha);
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao conectar ao banco de dados", e);
    }
  }

  public Connection getConn() {
    return conn;
  }

}
