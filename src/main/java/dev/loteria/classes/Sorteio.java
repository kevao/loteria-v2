package dev.loteria.classes;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Representa um sorteio de loteria, contendo os números sorteados e a
 * modalidade associada.
 * 
 * @author Kevin Villanova
 */
public class Sorteio {
  Set<Integer> numerosSorteados = new LinkedHashSet<>();
  Modalidade modalidade;

  public Sorteio(Modalidade modalidade) {
    this.modalidade = modalidade;
    this.sortear();
  }

  public Set<Integer> getNumerosSorteados() {
    return numerosSorteados;
  }

  public Modalidade getModalidade() {
    return modalidade;
  }

  /**
   * Realiza o sorteio completo da modalidade loteria escolhida.
   */
  private void sortear() {
    while (numerosSorteados.size() < modalidade.getnumerosSorteio()) {
      int bola = gerarBola();
      numerosSorteados.add(bola);
    }
  }

  /**
   * Gera um número aleatório representando uma bola sorteada.
   */
  private int gerarBola() {
    int min = modalidade.getMenorBola();
    int max = modalidade.getMaiorBola();

    return ThreadLocalRandom.current().nextInt(min, max + 1);
  }
}
