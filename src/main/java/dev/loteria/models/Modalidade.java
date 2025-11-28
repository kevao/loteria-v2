package dev.loteria.models;

import dev.loteria.interfaces.Modelo;

/**
 * Representa uma modalidade de loteria.
 * 
 * @author Kevin Villanova
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

  public java.util.UUID getId() {
    return id;
  }

  public void setId(java.util.UUID id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public int getNumerosSorteio() {
    return numerosSorteio;
  }

  public void setNumerosSorteio(int numerosSorteio) {
    this.numerosSorteio = numerosSorteio;
  }

  public int getMenorBola() {
    return menorBola;
  }

  public void setMenorBola(int menorBola) {
    this.menorBola = menorBola;
  }

  public int getMaiorBola() {
    return maiorBola;
  }

  public void setMaiorBola(int maiorBola) {
    this.maiorBola = maiorBola;
  }

  public double getValorJogo() {
    return valorJogo;
  }

  public void setValorJogo(double valorJogo) {
    this.valorJogo = valorJogo;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

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
