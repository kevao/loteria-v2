package dev.loteria.models;

abstract class Pagamento {
  private String descricao;
  private double valor;

  public Pagamento(String descricao, double valor) {
    this.descricao = descricao;
    this.valor = valor;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public double getValor() {
    return valor;
  }

  public void setValor(double valor) {
    this.valor = valor;
  }
}
