package dev.loteria.ui;

import dev.loteria.Loteria;
import dev.loteria.services.ModalidadeService;

public class MenuModalidades extends Menu {

  static {
    servico = new ModalidadeService();
  }

  public MenuModalidades() {
    this.opcaoMaxima = 4;
  }

  public void mostrarOpcoes() {
    System.out.println("\n====== MODALIDADE ======");
    System.out.println("1. Listar Modalidades");
    System.out.println("2. Inserir Modalidade");
    System.out.println("3. Atualizar Modalidade");
    System.out.println("4. Deletar Modalidade");
    System.out.println("0. Voltar ao menu principal");
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
