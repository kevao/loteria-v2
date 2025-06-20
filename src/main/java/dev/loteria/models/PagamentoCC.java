package dev.loteria.models;

public class PagamentoCC extends Pagamento {
  private int parcelas;
  private double valorParcela = 0;
  private static final int MAX_PARCELAS = 6;
  private static final int MIN_POR_PARCELA = 10;

  public PagamentoCC(String descricao, double valor, int parcelas) {
    super(descricao, valor);
    setParcelas(parcelas);
  }

  public int getParcelas() {
    return parcelas;
  }

  public void setParcelas(int parcelas) {

    if (parcelas <= 1) {
      this.parcelas = 1;
      return;
    }

    if (parcelas > MAX_PARCELAS) {
      parcelas = MAX_PARCELAS;
    }

    valorParcela = Math.ceil((getValor() / parcelas) * 100) / 100;

    if (valorParcela < MIN_POR_PARCELA) {
      setParcelas(parcelas - 1);
      return;
    }

    this.parcelas = parcelas;
  }

  @Override
  public String toString() {
    return "PagamentoCC{" +
        "descricao='" + getDescricao() + '\'' +
        ", valor=" + getValor() +
        ", parcelas=" + parcelas +
        ", valorParcela=" + valorParcela +
        '}';
  }
}
