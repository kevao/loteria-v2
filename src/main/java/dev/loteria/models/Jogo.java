package dev.loteria.models;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Representa uma compra de jogo realizada por um cliente para uma
 * determinada modalidade. Guarda referência à modalidade, cliente,
 * funcionário que efetuou a venda, os números jogados e a data da compra.
 */
public class Jogo {
  private java.util.UUID id;
  private Modalidade modalidade;
  private Cliente cliente;
  private Funcionario funcionario;
  private Set<Integer> numeros = new LinkedHashSet<>();
  private LocalDateTime dataCompra;

  public Jogo(Modalidade modalidade, Cliente cliente, Funcionario funcionario, Set<Integer> numeros) {
    this.modalidade = modalidade;
    this.cliente = cliente;
    this.funcionario = funcionario;
    this.numeros = numeros == null ? new LinkedHashSet<>() : numeros;
    this.dataCompra = LocalDateTime.now();
  }

  /**
   * Construtor para criar um novo registro de compra (sem `id`). A data de
   * compra será definida como o momento atual.
   *
   * @param modalidade  modalidade jogada
   * @param cliente     cliente que efetuou a compra
   * @param funcionario funcionário que registrou a venda
   * @param numeros     conjunto de números escolhidos
   */

  public Jogo(java.util.UUID id, Modalidade modalidade, Cliente cliente, Funcionario funcionario, Set<Integer> numeros,
      LocalDateTime dataCompra) {
    this.id = id;
    this.modalidade = modalidade;
    this.cliente = cliente;
    this.funcionario = funcionario;
    this.numeros = numeros == null ? new LinkedHashSet<>() : numeros;
    this.dataCompra = dataCompra;
  }

  /**
   * Construtor completo com `id` e data de compra explícita (ex.: leitura do DB).
   *
   * @param id          UUID do registro
   * @param modalidade  modalidade jogada
   * @param cliente     cliente
   * @param funcionario funcionário
   * @param numeros     conjunto de números
   * @param dataCompra  data/hora da compra
   */

  public java.util.UUID getId() {
    return id;
  }

  /**
   * Retorna o UUID do registro de compra.
   *
   * @return UUID ou `null`
   */

  public void setId(java.util.UUID id) {
    this.id = id;
  }

  /**
   * Define o UUID do registro.
   *
   * @param id UUID a atribuir
   */

  public Modalidade getModalidade() {
    return modalidade;
  }

  /**
   * Retorna a modalidade associada ao jogo.
   *
   * @return {@link Modalidade}
   */

  public void setModalidade(Modalidade modalidade) {
    this.modalidade = modalidade;
  }

  /**
   * Define a modalidade do jogo.
   *
   * @param modalidade nova modalidade
   */

  public Cliente getCliente() {
    return cliente;
  }

  /**
   * Retorna o cliente que comprou o jogo.
   *
   * @return {@link Cliente}
   */

  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
  }

  /**
   * Define o cliente associado ao jogo.
   *
   * @param cliente novo cliente
   */

  public Funcionario getFuncionario() {
    return funcionario;
  }

  /**
   * Retorna o funcionário que registrou a venda.
   *
   * @return {@link Funcionario}
   */

  public void setFuncionario(Funcionario funcionario) {
    this.funcionario = funcionario;
  }

  /**
   * Define o funcionário associado ao registro.
   *
   * @param funcionario funcionário
   */

  public Set<Integer> getNumeros() {
    return numeros;
  }

  /**
   * Retorna o conjunto de números jogados.
   *
   * @return conjunto de inteiros
   */

  public void setNumeros(Set<Integer> numeros) {
    this.numeros = numeros;
  }

  /**
   * Define o conjunto de números do jogo.
   *
   * @param numeros conjunto de inteiros
   */

  public LocalDateTime getDataCompra() {
    return dataCompra;
  }

  /**
   * Retorna a data/hora da compra.
   *
   * @return data de compra
   */

  public void setDataCompra(LocalDateTime dataCompra) {
    this.dataCompra = dataCompra;
  }

  /**
   * Define a data/hora da compra.
   *
   * @param dataCompra data a ser atribuída
   */

  @Override
  public String toString() {
    return "Jogo{" + "id=" + id + ", modalidade=" + (modalidade != null ? modalidade.getNome() : "null") + ", cliente="
        + (cliente != null ? cliente.getNome() : "null") + ", funcionario="
        + (funcionario != null ? funcionario.getNome() : "null")
        + ", numeros=" + numeros + ", dataCompra=" + dataCompra + '}';
  }
}
