package dev.loteria;

import java.util.Scanner;
import dev.loteria.database.Conexao;
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

  private static MenuPrincipal menuPrincipal = new MenuPrincipal();
  private static MenuModalidades menuModalidades = new MenuModalidades();
  private static MenuSorteios menuSorteios = new MenuSorteios();
  private static MenuJogos menuJogos = new MenuJogos();

  public static void main(String[] args) {
    new Conexao();
    menuPrincipal.init();
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
