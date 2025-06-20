package dev.loteria.ui;

import dev.loteria.Loteria;

public class MenuPrincipal extends Menu {

  public MenuPrincipal() {
    this.opcaoMaxima = 2;
  }

  @Override
  public void mostrarOpcoes() {
    System.out.println("\n========== MENU PRINCIPAL ==========");
    System.out.println("1. Modalidades");
    System.out.println("2. Sorteios");
    System.out.println("0. Sair");
  }

  @Override
  public void seguirOpcao() {
    escolherOpcao();
    switch (opcao) {
      case 1 -> Loteria.getMenuModalidades().init();
      case 2 -> Loteria.getMenuSorteios().init();
    }
  }

}
