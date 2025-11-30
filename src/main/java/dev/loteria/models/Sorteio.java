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
  java.util.UUID id;
  Set<Integer> numerosSorteados = new LinkedHashSet<>();
  Modalidade modalidade;
  /**
   * Representa um sorteio realizado para uma modalidade: contém referência
   * à modalidade, os números sorteados e a data/hora do sorteio. Usado pela
   * camada de persistência e pela UI para exibir resultados.
   */
  LocalDateTime horario;

  public Sorteio(Modalidade modalidade) {
    this.modalidade = modalidade;
    this.sortear();
    this.setHorario();
  }

  /**
   * Construtor que cria e realiza um sorteio para a modalidade informada.
   *
   * @param modalidade modalidade a ser sorteada
   */

  public Sorteio(java.util.UUID id, Modalidade modalidade) {
    this.id = id;
    this.modalidade = modalidade;
    this.sortear();
    this.setHorario();
  }

  /**
   * Construtor com `id` conhecido, usado ao recuperar sorteios persistidos.
   *
   * @param id         UUID do sorteio
   * @param modalidade modalidade sorteada
   */

  public Set<Integer> getNumerosSorteados() {
    return numerosSorteados;
  }

  /**
   * Retorna os números sorteados neste sorteio.
   *
   * @return conjunto de números sorteados
   */

  public Modalidade getModalidade() {
    return modalidade;
  }

  /**
   * Retorna a modalidade associada ao sorteio.
   *
   * @return {@link Modalidade}
   */

  public LocalDateTime getHorario() {
    return horario;
  }

  /**
   * Retorna a data/hora em que o sorteio foi realizado.
   *
   * @return data/hora do sorteio
   */

  public void setHorario() {
    this.horario = LocalDateTime.now();
  }

  /**
   * Define a data/hora do sorteio para o momento atual.
   */

  public java.util.UUID getId() {
    return id;
  }

  /**
   * Retorna o UUID do sorteio.
   *
   * @return UUID ou `null`
   */

  public void setId(java.util.UUID id) {
    this.id = id;
  }

  /**
   * Define o UUID do sorteio.
   *
   * @param id UUID a ser atribuído
   */

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
