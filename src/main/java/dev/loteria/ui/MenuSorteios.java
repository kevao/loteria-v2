package dev.loteria.ui;

import dev.loteria.Loteria;
import dev.loteria.services.ModalidadeService;

public class MenuSorteios extends Menu {

  static {
    servico = new ModalidadeService();
  }

  public MenuSorteios() {
    this.opcaoMaxima = 4;
  }

  public void mostrarOpcoes() {
    System.out.println("\n========== SORTEIOS ===========");
    System.out.println("1. Listar Sorteios");
    System.out.println("2. Inserir Sorteio");
    System.out.println("3. Atualizar Sorteio");
    System.out.println("4. Deletar Sorteio");
    System.out.println("0. Voltar ao menu principal");
    System.out.print("Escolha uma opção: ");
  }

  public void seguirOpcao() {
    escolherOpcao();
    switch (opcao) {
      case 1 -> servico.listar();
      case 2 -> servico.inserir();
      case 3 -> servico.editar();
      case 4 -> servico.deletar();
      case 0 -> Loteria.getMenuPrincipal().init();
    }
  }

}
