package dev.loteria.ui;

public class MenuSorteios extends Menu {

  public MenuSorteios() {
    this.opcaoMaxima = 4;
  }

  public void mostrarOpcoes() {
    System.out.println("====== SORTEIOS ======");
    System.out.println("1. Mega-Sena");
    System.out.println("2. Quina");
    System.out.println("3. Lotofácil");
    System.out.println("4. Lotomania");
    System.out.println("0. Voltar ao menu principal");
    System.out.print("Escolha uma opção: ");
  }

  public void seguirOpcao() {
    // Menu.escolherOpcao();
    // switch (opcao) {
    // case 1 -> System.out.println("Você escolheu Mega-Sena.");
    // case 2 -> System.out.println("Você escolheu Quina.");
    // case 3 -> System.out.println("Você escolheu Lotofácil.");
    // case 4 -> System.out.println("Você escolheu Lotomania.");
    // case 0 -> MenuPrincipal.init();
    // default -> System.out.println("Opção inválida.");
    // }
  }

}
