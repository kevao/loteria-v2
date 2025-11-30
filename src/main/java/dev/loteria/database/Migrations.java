package dev.loteria.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Monta as tabelas do banco de dados, se não existirem.
 * 
 * @author Kevin Villanova
 */
public class Migrations {

  /**
   * Executa as migrations necessárias.
   */
  public static void run() {
    Connection conn = Conexao.getConn();
    try (Statement st = conn.createStatement()) {
      // enable uuid extension
      st.execute("CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\"");

      // modalidades
      String modalidades = "CREATE TABLE IF NOT EXISTS modalidades (" +
          "id UUID PRIMARY KEY DEFAULT uuid_generate_v4()," +
          "nome VARCHAR(50) NOT NULL," +
          "numeros_sorteio INT NOT NULL," +
          "menor_bola INT NOT NULL," +
          "maior_bola INT NOT NULL," +
          "valor_jogo DOUBLE PRECISION NOT NULL," +
          "descricao VARCHAR(255) NOT NULL," +
          "ativo BOOLEAN NOT NULL DEFAULT true" +
          ")";
      st.execute(modalidades);
      st.execute("ALTER TABLE modalidades ADD COLUMN IF NOT EXISTS ativo BOOLEAN NOT NULL DEFAULT true");
      st.execute("CREATE UNIQUE INDEX IF NOT EXISTS ux_modalidades_nome ON modalidades (LOWER(nome))");

      // clientes
      String clientes = "CREATE TABLE IF NOT EXISTS clientes (" +
          "id UUID PRIMARY KEY DEFAULT uuid_generate_v4()," +
          "nome VARCHAR(100) NOT NULL," +
          "email VARCHAR(120)," +
          "telefone VARCHAR(40)," +
          "endereco VARCHAR(255)," +
          "cpf VARCHAR(20)," +
          "ativo BOOLEAN NOT NULL DEFAULT true" +
          ")";
      st.execute(clientes);

      // funcionarios
      String funcionarios = "CREATE TABLE IF NOT EXISTS funcionarios (" +
          "id UUID PRIMARY KEY DEFAULT uuid_generate_v4()," +
          "nome VARCHAR(100) NOT NULL," +
          "email VARCHAR(120)," +
          "telefone VARCHAR(40)," +
          "endereco VARCHAR(255)," +
          "cpf VARCHAR(20)," +
          "ativo BOOLEAN NOT NULL DEFAULT true," +
          "matricula VARCHAR(50)," +
          "cargo VARCHAR(80)," +
          "salario DOUBLE PRECISION" +
          ")";
      st.execute(funcionarios);

      // sorteios
      String sorteios = "CREATE TABLE IF NOT EXISTS sorteios (" +
          "id UUID PRIMARY KEY DEFAULT uuid_generate_v4()," +
          "modalidade_id UUID NOT NULL," +
          "numeros_sorteados TEXT NOT NULL," +
          "horario TIMESTAMP NOT NULL," +
          "FOREIGN KEY (modalidade_id) REFERENCES modalidades(id)" +
          ")";
      st.execute(sorteios);

      // jogos
      String jogos = "CREATE TABLE IF NOT EXISTS jogos (" +
          "id UUID PRIMARY KEY DEFAULT uuid_generate_v4()," +
          "modalidade_id UUID NOT NULL," +
          "cliente_id UUID NOT NULL," +
          "funcionario_id UUID NOT NULL," +
          "numeros TEXT," +
          "data_compra TIMESTAMP NOT NULL DEFAULT now()," +
          "FOREIGN KEY (modalidade_id) REFERENCES modalidades(id)," +
          "FOREIGN KEY (cliente_id) REFERENCES clientes(id)," +
          "FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id)" +
          ")";
      st.execute(jogos);

      System.out.println("Migrations: tabelas verificadas/criadas com sucesso.");
    } catch (SQLException ex) {
      System.out.println("Migrations: falha ao aplicar DDLs - " + ex.getMessage());
      ex.printStackTrace();
    }
  }
}
