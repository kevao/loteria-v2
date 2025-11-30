package dev.loteria.models;

import dev.loteria.interfaces.Modelo;

/**
 * Representa a entidade base para pessoas no sistema. Serve como classe
 * mãe para {@link Cliente} e {@link Funcionario} e contém atributos
 * comuns como nome, email, telefone, endereço, CPF e flag de ativo.
 */
public abstract class Pessoa implements Modelo {
  private java.util.UUID id;
  private String nome;
  private String email;
  private String telefone;
  private String endereco;
  private String cpf;
  private boolean ativo = true;

  public Pessoa() {
  }

  /**
   * Construtor padrão.
   */

  public Pessoa(String nome, String email, String telefone) {
    this.nome = nome;
    this.email = email;
    this.telefone = telefone;
  }

  /**
   * Construtor para criar pessoa com os campos básicos.
   *
   * @param nome     nome completo
   * @param email    e-mail
   * @param telefone telefone
   */

  public Pessoa(String nome, String email, String telefone, String endereco, String cpf, boolean ativo) {
    this.nome = nome;
    this.email = email;
    this.telefone = telefone;
    this.endereco = endereco;
    this.cpf = cpf;
    this.ativo = ativo;
  }

  /**
   * Construtor completo com endereço, CPF e flag de ativo.
   *
   * @param nome     nome completo
   * @param email    e-mail
   * @param telefone telefone
   * @param endereco endereço completo
   * @param cpf      CPF
   * @param ativo    flag indicando se está ativo
   */

  public Pessoa(java.util.UUID id, String nome, String email, String telefone) {
    this.id = id;
    this.nome = nome;
    this.email = email;
    this.telefone = telefone;
  }

  /**
   * Construtor usado quando o UUID já é conhecido (ex.: leitura do DB).
   *
   * @param id       UUID
   * @param nome     nome
   * @param email    e-mail
   * @param telefone telefone
   */

  @Override
  public java.util.UUID getId() {
    return id;
  }

  /**
   * Retorna o identificador UUID da entidade.
   *
   * @return UUID ou `null` se não atribuído
   */

  @Override
  public void setId(java.util.UUID id) {
    this.id = id;
  }

  /**
   * Define o UUID da entidade.
   *
   * @param id UUID a ser atribuído
   */

  public String getNome() {
    return nome;
  }

  /**
   * Retorna o nome da pessoa.
   *
   * @return nome
   */

  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   * Define o nome da pessoa.
   *
   * @param nome novo nome
   */

  public String getEmail() {
    return email;
  }

  /**
   * Retorna o e-mail da pessoa.
   *
   * @return e-mail
   */

  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Define o e-mail da pessoa.
   *
   * @param email novo e-mail
   */

  public String getTelefone() {
    return telefone;
  }

  /**
   * Retorna o telefone de contato.
   *
   * @return telefone
   */

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  /**
   * Define o telefone de contato.
   *
   * @param telefone novo telefone
   */

  public String getEndereco() {
    return endereco;
  }

  /**
   * Retorna o endereço completo.
   *
   * @return endereço
   */

  public void setEndereco(String endereco) {
    this.endereco = endereco;
  }

  /**
   * Define o endereço da pessoa.
   *
   * @param endereco novo endereço
   */

  public String getCpf() {
    return cpf;
  }

  /**
   * Retorna o CPF da pessoa.
   *
   * @return CPF
   */

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  /**
   * Define o CPF da pessoa.
   *
   * @param cpf novo CPF
   */

  public boolean isAtivo() {
    return ativo;
  }

  /**
   * Indica se a pessoa está ativa no sistema.
   *
   * @return `true` se ativa
   */

  public void setAtivo(boolean ativo) {
    this.ativo = ativo;
  }

  /**
   * Define o sinalizador de ativo.
   *
   * @param ativo novo estado
   */

  @Override
  public String toString() {
    return "Pessoa{" + "id=" + id + ", nome='" + nome + '\'' + ", email='" + email + '\'' + ", telefone='" + telefone
        + '\'' + '}';
  }
}
