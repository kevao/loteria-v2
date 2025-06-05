package dev.loteria.classes;

/**
 * Representa uma modalidade de loteria.
 * 
 * @author Kevin Villanova
 */
public class Modalidade {
  private int id;
  private String nome;
  private int numerosSorteio;
  private int menorBola;
  private int maiorBola;
  private String descricao;

  public Modalidade(String nome, int numerosSorteio, int menorBola, int maiorBola, String descricao) {
    this.nome = nome;
    this.numerosSorteio = numerosSorteio;
    this.menorBola = menorBola;
    this.maiorBola = maiorBola;
    this.descricao = descricao;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public int getnumerosSorteio() {
    return numerosSorteio;
  }

  public void setnumerosSorteio(int numerosSorteio) {
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

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }
}
