package dev.loteria.ui;

import java.util.InputMismatchException;
import dev.loteria.Loteria;
import dev.loteria.interfaces.Servico;

public abstract class Menu {

  protected int opcao = 99;
  protected int opcaoMaxima = 5;
  protected static Servico servico;

  public void init() {
    mostrarOpcoes();
    seguirOpcao();
  }

  public void escolherOpcao() {
    System.out.print("\nEscolha uma opção: ");

    try {
      opcao = Loteria.SCANNER.nextInt();
    } catch (InputMismatchException e) {
      System.out.println("Opção inválida. Por favor, insira um número.");
      Loteria.SCANNER.nextLine();
      escolherOpcao();
      return;
    }

    if (opcao < 0 || opcao > opcaoMaxima) {
      System.out.println("Opção inválida. Por favor, escolha uma opção entre 0 e " + opcaoMaxima + ".");
      escolherOpcao();
      return;
    }
  }

  abstract void mostrarOpcoes();

  abstract void seguirOpcao();
}
