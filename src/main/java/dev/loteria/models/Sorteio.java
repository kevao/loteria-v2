package dev.loteria.models;

import java.time.LocalDateTime;
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
  int id;
  Set<Integer> numerosSorteados = new LinkedHashSet<>();
  Modalidade modalidade;
  LocalDateTime horario;

  public Sorteio(Modalidade modalidade) {
    this.modalidade = modalidade;
    this.sortear();
    this.setHorario();
  }

  public Sorteio(int id, Modalidade modalidade) {
    this.id = id;
    this.modalidade = modalidade;
    this.sortear();
    this.setHorario();
  }

  public Set<Integer> getNumerosSorteados() {
    return numerosSorteados;
  }

  public Modalidade getModalidade() {
    return modalidade;
  }

  public LocalDateTime getHorario() {
    return horario;
  }

  public void setHorario() {
    this.horario = LocalDateTime.now();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * Realiza o sorteio completo da modalidade loteria escolhida.
   */
  private void sortear() {
    while (numerosSorteados.size() < modalidade.getNumerosSorteio()) {
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

  @Override
  public String toString() {
    return "Sorteio{" +
        "id=" + id +
        ", numerosSorteados=" + numerosSorteados +
        ", modalidade=" + (modalidade != null ? modalidade.getNome() : "null") +
        ", horario=" + horario +
        '}';
  }
}
