package dev.loteria.models;

import dev.loteria.interfaces.Modelo;

/**
 * Representa uma modalidade de loteria (ex: Mega-Sena, Quina, Lotofácil,
 * Lotomania). Contém configurações como quantidade de números sorteados,
 * menor/maior bola, preço do jogo e descrição.
 */
public class Modalidade implements Modelo {
  private java.util.UUID id;
  private String nome;
  private int numerosSorteio;
  private int menorBola;
  private int maiorBola;
  private String descricao;
  private double valorJogo;

  public Modalidade(String nome, int numerosSorteio, int menorBola, int maiorBola, double valorJogo, String descricao) {
    this.nome = nome;
    this.numerosSorteio = numerosSorteio;
    this.menorBola = menorBola;
    this.maiorBola = maiorBola;
    this.valorJogo = valorJogo;
    this.descricao = descricao;
  }

  /**
   * Cria uma nova modalidade (sem `id`).
   *
   * @param nome           nome da modalidade
   * @param numerosSorteio quantidade de números sorteados
   * @param menorBola      menor número possível
   * @param maiorBola      maior número possível
   * @param valorJogo      preço do jogo
   * @param descricao      texto descritivo
   */

  public Modalidade(java.util.UUID id, String nome, int numerosSorteio, int menorBola, int maiorBola, double valorJogo,
      String descricao) {
    this.id = id;
    this.nome = nome;
    this.numerosSorteio = numerosSorteio;
    this.menorBola = menorBola;
    this.maiorBola = maiorBola;
    this.valorJogo = valorJogo;
    this.descricao = descricao;
  }

  /**
   * Construtor com `id` conhecido (ex.: leitura do DB).
   *
   * @param id             UUID
   * @param nome           nome da modalidade
   * @param numerosSorteio quantidade de números sorteados
   * @param menorBola      menor número
   * @param maiorBola      maior número
   * @param valorJogo      preço do jogo
   * @param descricao      descrição
   */

  public java.util.UUID getId() {
    return id;
  }

  /**
   * Retorna o UUID da modalidade.
   *
   * @return UUID ou `null`
   */

  public void setId(java.util.UUID id) {
    this.id = id;
  }

  /**
   * Define o UUID da modalidade.
   *
   * @param id UUID a ser atribuído
   */

  public String getNome() {
    return nome;
  }

  /**
   * Retorna o nome da modalidade.
   *
   * @return nome
   */

  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   * Define o nome da modalidade.
   *
   * @param nome novo nome
   */

  public int getNumerosSorteio() {
    return numerosSorteio;
  }

  /**
   * Retorna quantos números são sorteados nessa modalidade.
   *
   * @return quantidade de números do sorteio
   */

  public void setNumerosSorteio(int numerosSorteio) {
    this.numerosSorteio = numerosSorteio;
  }

  /**
   * Define a quantidade de números sorteados para a modalidade.
   *
   * @param numerosSorteio nova quantidade
   */

  public int getMenorBola() {
    return menorBola;
  }

  /**
   * Retorna o menor número permitido na modalidade.
   *
   * @return menor bola
   */

  public void setMenorBola(int menorBola) {
    this.menorBola = menorBola;
  }

  /**
   * Define o menor número permitido.
   *
   * @param menorBola novo menor número
   */

  public int getMaiorBola() {
    return maiorBola;
  }

  /**
   * Retorna o maior número permitido na modalidade.
   *
   * @return maior bola
   */

  public void setMaiorBola(int maiorBola) {
    this.maiorBola = maiorBola;
  }

  /**
   * Define o maior número permitido.
   *
   * @param maiorBola novo maior número
   */

  public double getValorJogo() {
    return valorJogo;
  }

  /**
   * Retorna o preço do jogo nessa modalidade.
   *
   * @return valor do jogo
   */

  public void setValorJogo(double valorJogo) {
    this.valorJogo = valorJogo;
  }

  /**
   * Define o preço do jogo.
   *
   * @param valorJogo novo valor
   */

  public String getDescricao() {
    return descricao;
  }

  /**
   * Retorna a descrição da modalidade.
   *
   * @return descrição
   */

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  /**
   * Define a descrição da modalidade.
   *
   * @param descricao texto descritivo
   */

  @Override
  public String toString() {
    return "Modalidade{" +
        "id=" + id +
        ", nome='" + nome + '\'' +
        ", numerosSorteio=" + numerosSorteio +
        ", menorBola=" + menorBola +
        ", maiorBola=" + maiorBola +
        ", valorJogo=" + valorJogo +
        ", descricao='" + descricao + '\'' +
        '}';
  }
}
