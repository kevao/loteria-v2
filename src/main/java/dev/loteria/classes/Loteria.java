package dev.loteria.classes;

import java.util.ArrayList;

/**
 * Classe representando a loteria.
 * 
 * @author Kevin Villanova
 */
public class Loteria {
  private ArrayList<Modalidade> modalidades = new ArrayList<>();
  private ArrayList<Sorteio> sorteios = new ArrayList<>();

  public Loteria() {
    this.gerarModalidades();
    this.gerarSorteios();

    for (Sorteio sorteio : sorteios) {
      System.out.println("Modalidade: " + sorteio.getModalidade().getNome());
      System.out.println("Números Sorteados: " + sorteio.getNumerosSorteados());
      System.out.println();
    }
  }

  public void gerarModalidades() {
    this.modalidades.add(new Modalidade("Mega-Sena", 6, 1, 60, "O jogo mais famoso do Brasil."));
    this.modalidades.add(new Modalidade("Quina", 5, 1, 80, "Jogo popular com sorteio de 5 números."));
    this.modalidades.add(new Modalidade("Lotofácil", 15, 1, 25, "Fácil de jogar, fácil de ganhar."));
    this.modalidades.add(new Modalidade("Lotomania", 20, 0, 99, "Jogo com 20 números sorteados entre 0 e 99."));
  }

  public void gerarSorteios() {
    for (Modalidade modalidade : modalidades) {
      Sorteio sorteio = new Sorteio(modalidade);
      this.sorteios.add(sorteio);
    }
  }

  // public void setModalidade(Modalidade modalidade) {
  // this.modalidade = modalidade;
  // }
}
