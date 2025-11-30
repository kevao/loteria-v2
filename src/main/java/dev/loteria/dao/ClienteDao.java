package dev.loteria.dao;

import dev.loteria.database.Conexao;
import dev.loteria.interfaces.CRUD;
import dev.loteria.models.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO responsável por operações de persistência para a entidade
 * {@link Cliente}.
 * 
 * @author Kevin Villanova
 */
public class ClienteDao implements CRUD<Cliente> {

  private final Connection conn;
  private PreparedStatement ps;

  public ClienteDao() {
    this.conn = Conexao.getConn();
  }

  /**
   * Listagem de todos os clientes cadastrados.
   *
   * @return {@link ResultSet} com todos os clientes, ou `null` em caso de erro
   */
  public ResultSet listar() {
    try {
      return conn.createStatement().executeQuery("SELECT * FROM clientes");
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao listar clientes: " + e.getMessage());
    }
    return null;
  }

  /**
   * Insere um novo cliente na base de dados.
   * 
   * @param modelo instância de {@link Cliente} a ser persistida
   */
  public void inserir(Cliente modelo) {
    try {
      String sql = "INSERT INTO clientes (nome, email, telefone, endereco, cpf, ativo) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
      ps = conn.prepareStatement(sql);
      ps.setString(1, modelo.getNome());
      ps.setString(2, modelo.getEmail());
      ps.setString(3, modelo.getTelefone());
      ps.setString(4, modelo.getEndereco());
      ps.setString(5, modelo.getCpf());
      ps.setBoolean(6, modelo.isAtivo());
      ResultSet rs = ps.executeQuery();
      if (rs != null && rs.next()) {
        java.util.UUID id = rs.getObject("id", java.util.UUID.class);
        modelo.setId(id);
      }
      if (rs != null)
        rs.close();
      ps.close();
      System.out.println("Cliente inserido com sucesso!");
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao inserir o cliente: " + e.getMessage());
    }
  }

  /**
   * Atualiza os dados de um cliente existente.
   *
   * @param modelo instância de {@link Cliente} com `id` e novos valores
   */
  public void editar(Cliente modelo) {
    if (!checkId(modelo.getId())) {
      System.out.println("Cliente com ID " + modelo.getId() + " não existe.");
      return;
    }
    try {
      String sql = "UPDATE clientes SET nome = ?, email = ?, telefone = ?, endereco = ?, cpf = ?, ativo = ? WHERE id = ?";
      ps = conn.prepareStatement(sql);
      ps.setString(1, modelo.getNome());
      ps.setString(2, modelo.getEmail());
      ps.setString(3, modelo.getTelefone());
      ps.setString(4, modelo.getEndereco());
      ps.setString(5, modelo.getCpf());
      ps.setBoolean(6, modelo.isAtivo());
      ps.setObject(7, modelo.getId());
      ps.executeUpdate();
      ps.close();
      System.out.println("Cliente atualizado com sucesso!");
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao editar o cliente: " + e.getMessage());
    }
  }

  /**
   * Remove o cliente identificado por `id` da base de dados.
   * 
   * @param id UUID do cliente a ser removido.
   */
  public void deletar(java.util.UUID id) {
    if (!checkId(id)) {
      System.out.println("Cliente com ID " + id + " não existe.");
      return;
    }
    try {
      String sql = "DELETE FROM clientes WHERE id = ?";
      ps = conn.prepareStatement(sql);
      ps.setObject(1, id);
      ps.executeUpdate();
      ps.close();
      System.out.println("Cliente deletado com sucesso!");
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao deletar o cliente: " + e.getMessage());
    }
  }

  /**
   * Conta quantos registros existem na tabela `clientes`.
   *
   * @return número total de clientes (0 em caso de erro)
   */
  public int contar() {
    int count = 0;
    try {
      String sql = "SELECT COUNT(*) FROM clientes";
      ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      if (rs.next())
        count = rs.getInt(1);
      rs.close();
      ps.close();
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao contar clientes: " + e.getMessage());
    }
    return count;
  }

  /**
   * Verifica se um cliente com o `id` informado existe na base.
   *
   * @param id UUID a verificar
   * @return `true` se o cliente existir, `false` caso contrário ou em erro
   */
  public boolean checkId(java.util.UUID id) {
    try {
      String sql = "SELECT 1 FROM clientes WHERE id = ?";
      ps = conn.prepareStatement(sql);
      ps.setObject(1, id);
      ResultSet rs = ps.executeQuery();
      boolean existe = rs.next();
      rs.close();
      ps.close();
      return existe;
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao verificar o ID do cliente: " + e.getMessage());
    }
    return false;
  }

  /**
   * Recupera um {@link Cliente} pelo seu UUID. Retorna `null` se o cliente
   * não existir ou se ocorrer um erro durante a consulta.
   *
   * @param id UUID do cliente a recuperar
   * @return instância de {@link Cliente} ou `null`
   */
  public Cliente getById(java.util.UUID id) {
    if (!checkId(id))
      return null;
    try {
      String sql = "SELECT * FROM clientes WHERE id = ?";
      ps = conn.prepareStatement(sql);
      ps.setObject(1, id);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        Cliente c = new Cliente(
            rs.getObject("id", java.util.UUID.class),
            rs.getString("nome"),
            rs.getString("email"),
            rs.getString("telefone"),
            rs.getString("endereco"),
            rs.getString("cpf"),
            rs.getBoolean("ativo"));
        rs.close();
        ps.close();
        return c;
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao buscar cliente por ID: " + e.getMessage());
    }
    return null;
  }
}
