package dev.loteria.dao;

import dev.loteria.database.Conexao;
import dev.loteria.interfaces.CRUD;
import dev.loteria.models.Funcionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO para persistência de {@link dev.loteria.models.Funcionario}. Expõe
 * operações de CRUD e utilitários para gestão de funcionários.
 */
public class FuncionarioDao implements CRUD<Funcionario> {

  private final Connection conn;
  private PreparedStatement ps;

  public FuncionarioDao() {
    this.conn = Conexao.getConn();
  }

  /**
   * Construtor que inicializa o DAO usando a conexão compartilhada.
   */

  /**
   * Retorna todos os funcionários cadastrados como um {@link ResultSet}.
   *
   * @return {@link ResultSet} com os funcionários ou `null` em caso de erro
   */
  public ResultSet listar() {
    try {
      return conn.createStatement().executeQuery("SELECT * FROM funcionarios");
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao listar funcionários: " + e.getMessage());
    }
    return null;
  }

  /**
   * Insere registros de exemplo na tabela `funcionarios` caso esteja vazia.
   */
  public void criarMockups() {
    if (contar() != 0)
      return;
    System.out.println("Criando mockups de funcionários...");
    inserir(new Funcionario("Carlos Almeida", "carlos@example.com", "(11) 91234-5678", "Rua do Comércio, 10",
        "404.404.404-40", true, "MTR001", "Atendente", 2200.0));
    inserir(new Funcionario("Fernanda Lima", "fernanda@example.com", "(21) 99876-5432", "Av. Central, 22",
        "505.505.505-50", true, "MTR002", "Caixa", 2500.0));
  }

  /**
   * Persiste um novo {@link Funcionario} no banco de dados.
   * Após a inserção, o campo `id` do objeto será preenchido com o UUID
   * retornado pelo banco quando disponível.
   *
   * @param f instância de {@link Funcionario} com os dados a serem gravados
   */
  public void inserir(Funcionario f) {
    try {
      String sql = "INSERT INTO funcionarios (nome, email, telefone, endereco, cpf, ativo, matricula, cargo, salario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
      ps = conn.prepareStatement(sql);
      ps.setString(1, f.getNome());
      ps.setString(2, f.getEmail());
      ps.setString(3, f.getTelefone());
      ps.setString(4, f.getEndereco());
      ps.setString(5, f.getCpf());
      ps.setBoolean(6, f.isAtivo());
      ps.setString(7, f.getMatricula());
      ps.setString(8, f.getCargo());
      ps.setDouble(9, f.getSalario());
      ResultSet rs = ps.executeQuery();
      if (rs != null && rs.next()) {
        java.util.UUID id = rs.getObject("id", java.util.UUID.class);
        f.setId(id);
      }
      if (rs != null)
        rs.close();
      ps.close();
      System.out.println("Funcionário inserido com sucesso!");
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao inserir o funcionário: " + e.getMessage());
    }
  }

  /**
   * Atualiza os dados de um funcionário existente. Se o ID não existir, a
   * operação é abortada.
   *
   * @param f instância de {@link Funcionario} com ID e novos valores
   */
  public void editar(Funcionario f) {
    if (!checkId(f.getId())) {
      System.out.println("Funcionário com ID " + f.getId() + " não existe.");
      return;
    }
    try {
      String sql = "UPDATE funcionarios SET nome = ?, email = ?, telefone = ?, endereco = ?, cpf = ?, ativo = ?, matricula = ?, cargo = ?, salario = ? WHERE id = ?";
      ps = conn.prepareStatement(sql);
      ps.setString(1, f.getNome());
      ps.setString(2, f.getEmail());
      ps.setString(3, f.getTelefone());
      ps.setString(4, f.getEndereco());
      ps.setString(5, f.getCpf());
      ps.setBoolean(6, f.isAtivo());
      ps.setString(7, f.getMatricula());
      ps.setString(8, f.getCargo());
      ps.setDouble(9, f.getSalario());
      ps.setObject(10, f.getId());
      ps.executeUpdate();
      ps.close();
      System.out.println("Funcionário atualizado com sucesso!");
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao editar o funcionário: " + e.getMessage());
    }
  }

  /**
   * Remove o funcionário identificado por `id` da base.
   *
   * @param id UUID do funcionário a ser removido
   */
  public void deletar(java.util.UUID id) {
    if (!checkId(id)) {
      System.out.println("Funcionário com ID " + id + " não existe.");
      return;
    }
    try {
      String sql = "DELETE FROM funcionarios WHERE id = ?";
      ps = conn.prepareStatement(sql);
      ps.setObject(1, id);
      ps.executeUpdate();
      ps.close();
      System.out.println("Funcionário deletado com sucesso!");
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao deletar o funcionário: " + e.getMessage());
    }
  }

  /**
   * Retorna a quantidade de funcionários registrados.
   *
   * @return número total de funcionários (0 em caso de erro)
   */
  public int contar() {
    int count = 0;
    try {
      String sql = "SELECT COUNT(*) FROM funcionarios";
      ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      if (rs.next())
        count = rs.getInt(1);
      rs.close();
      ps.close();
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao contar funcionários: " + e.getMessage());
    }
    return count;
  }

  /**
   * Verifica se um funcionário com o `id` informado existe.
   *
   * @param id UUID a verificar
   * @return `true` se existir, `false` caso contrário
   */
  public boolean checkId(java.util.UUID id) {
    try {
      String sql = "SELECT 1 FROM funcionarios WHERE id = ?";
      ps = conn.prepareStatement(sql);
      ps.setObject(1, id);
      ResultSet rs = ps.executeQuery();
      boolean existe = rs.next();
      rs.close();
      ps.close();
      return existe;
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao verificar o ID do funcionário: " + e.getMessage());
    }
    return false;
  }

  /**
   * Recupera um {@link Funcionario} pelo seu UUID.
   *
   * @param id UUID do funcionário
   * @return {@link Funcionario} ou `null` se não encontrado/erro
   */
  public Funcionario getById(java.util.UUID id) {
    if (!checkId(id))
      return null;
    try {
      String sql = "SELECT * FROM funcionarios WHERE id = ?";
      ps = conn.prepareStatement(sql);
      ps.setObject(1, id);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        Funcionario f = new Funcionario(
            rs.getObject("id", java.util.UUID.class),
            rs.getString("nome"),
            rs.getString("email"),
            rs.getString("telefone"),
            rs.getString("endereco"),
            rs.getString("cpf"),
            rs.getBoolean("ativo"),
            rs.getString("matricula"),
            rs.getString("cargo"),
            rs.getDouble("salario"));
        rs.close();
        ps.close();
        return f;
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao buscar funcionário por ID: " + e.getMessage());
    }
    return null;
  }
}
