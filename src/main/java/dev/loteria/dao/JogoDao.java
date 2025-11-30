package dev.loteria.dao;

import dev.loteria.database.Conexao;
import dev.loteria.models.Jogo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

/**
 * DAO para a entidade {@link dev.loteria.models.Jogo}. Responsável por
 * persistir compras de jogos realizadas por clientes (inserção e contagem).
 */
public class JogoDao {
  private final Connection conn;

  public JogoDao() throws SQLException {
    this.conn = Conexao.getConn();
  }

  /**
   * Construtor do DAO de jogos, inicializa a conexão.
   *
   * @throws SQLException se ocorrer erro na obtenção da conexão
   */

  /**
   * Persiste um novo {@link Jogo} (compra) no banco de dados. Caso o
   * atributo `id` do jogo seja `null`, um novo UUID será gerado localmente.
   * O conjunto de números é armazenado como texto CSV.
   *
   * @param jogo instância de {@link Jogo} com dados para inserção
   * @return UUID gerado/atribuído ao registro
   * @throws SQLException em caso de erro durante a operação
   */
  public UUID inserir(Jogo jogo) throws SQLException {
    String sql = "INSERT INTO jogos (id, modalidade_id, cliente_id, funcionario_id, numeros, data_compra) VALUES (?, ?, ?, ?, ?, ?)";
    UUID id = jogo.getId() == null ? UUID.randomUUID() : jogo.getId();
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setObject(1, id);
      ps.setObject(2, jogo.getModalidade().getId());
      ps.setObject(3, jogo.getCliente().getId());
      ps.setObject(4, jogo.getFuncionario().getId());
      ps.setString(5, asString(jogo.getNumeros()));
      ps.setTimestamp(6, Timestamp.valueOf(jogo.getDataCompra()));
      ps.executeUpdate();
    }
    return id;
  }

  /**
   * Converte um conjunto de números em uma string separada por vírgulas.
   * Retorna string vazia se a coleção for nula ou vazia.
   *
   * @param numeros conjunto de inteiros representando os números do jogo
   * @return representação CSV dos números
   */
  private String asString(Set<Integer> numeros) {
    if (numeros == null || numeros.isEmpty())
      return "";
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (Integer n : numeros) {
      if (!first)
        sb.append(',');
      sb.append(n);
      first = false;
    }
    return sb.toString();
  }

  /**
   * Conta quantos registros de jogos existem na tabela `jogos`.
   *
   * @return total de jogos
   * @throws SQLException em caso de erro na consulta
   */
  public int contar() throws SQLException {
    String sql = "SELECT count(*) FROM jogos";
    try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
      if (rs.next())
        return rs.getInt(1);
    }
    return 0;
  }

  /**
   * Retorna todos os jogos como um {@link ResultSet}. A carga de objetos
   * completos (Modalidade/Cliente/Funcionario) pode ser feita pelo
   * controller utilizando os respectivos DAOs.
   *
   * @return ResultSet com os registros de `jogos` ou `null` em caso de erro
   */
  public ResultSet listar() {
    try {
      return conn.createStatement().executeQuery("SELECT * FROM jogos ORDER BY data_compra DESC");
    } catch (SQLException e) {
      System.out.println("Ocorreu um erro ao listar jogos: " + e.getMessage());
    }
    return null;
  }
}
