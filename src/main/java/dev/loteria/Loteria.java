package dev.loteria;

import java.util.ArrayList;
import java.util.Scanner;

import dev.loteria.database.Conexao;
import dev.loteria.models.Modalidade;
import dev.loteria.models.Sorteio;
import dev.loteria.ui.MenuJogos;
import dev.loteria.ui.MenuModalidades;
import dev.loteria.ui.MenuPrincipal;
import dev.loteria.ui.MenuSorteios;

/**
 * Classe representando a loteria.
 * 
 * @author Kevin Villanova
 */
public final class Loteria {
  public final static Scanner SCANNER = new Scanner(System.in);

  private static ArrayList<Modalidade> modalidades = new ArrayList<>();
  private static ArrayList<Sorteio> sorteios = new ArrayList<>();

  private static MenuPrincipal menuPrincipal = new MenuPrincipal();
  private static MenuModalidades menuModalidades = new MenuModalidades();
  private static MenuSorteios menuSorteios = new MenuSorteios();
  private static MenuJogos menuJogos = new MenuJogos();

  public static void main(String[] args) {
    gerarModalidades();
    gerarSorteios();

    new Conexao();

    menuPrincipal.init();

    for (Sorteio sorteio : sorteios) {
      System.out.println("Modalidade: " + sorteio.getModalidade().getNome());
      System.out.println("Números Sorteados: " + sorteio.getNumerosSorteados());
      System.out.println();
    }
  }

  public static void gerarModalidades() {
    modalidades.add(new Modalidade("Mega-Sena", 6, 1, 60, 6.0, "O jogo mais famoso do Brasil."));
    modalidades.add(new Modalidade("Quina", 5, 1, 80, 4.5, "Jogo popular com sorteio de 5 números."));
    modalidades.add(new Modalidade("Lotofácil", 15, 1, 25, 5.0, "Fácil de jogar, fácil de ganhar."));
    modalidades.add(new Modalidade("Lotomania", 20, 0, 99, 2.5, "Jogo com 20 números sorteados entre 0 e 99."));
  }

  public static void gerarSorteios() {
    for (Modalidade modalidade : modalidades) {
      Sorteio sorteio = new Sorteio(modalidade);
      sorteios.add(sorteio);
    }
  }

  public static MenuPrincipal getMenuPrincipal() {
    return menuPrincipal;
  }

  public static MenuModalidades getMenuModalidades() {
    return menuModalidades;
  }

  public static MenuSorteios getMenuSorteios() {
    return menuSorteios;
  }

  public static MenuJogos getMenuJogos() {
    return menuJogos;
  }
}
