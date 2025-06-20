package dev.loteria.models;

public class PagamentoPIX extends Pagamento {
  private String chavePIX;

  public PagamentoPIX(String descricao, double valor, String chavePIX) {
    super(descricao, valor);
    this.chavePIX = chavePIX;
  }

  public String getChavePIX() {
    return chavePIX;
  }

  public void setChavePIX(String chavePIX) {
    this.chavePIX = chavePIX;
  }

  @Override
  public String toString() {
    return "PagamentoPIX{" +
        "descricao='" + getDescricao() + '\'' +
        ", valor=" + getValor() +
        ", chavePIX='" + chavePIX + '\'' +
        '}';
  }

}
