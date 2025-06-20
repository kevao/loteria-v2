package dev.loteria.services;

import dev.loteria.database.Loteria;
import dev.loteria.interfaces.Servico;

public class ModalidadeService implements Servico {

  public void listar() {
    retornarMenu();
  }

  public void inserir() {
    retornarMenu();
  }

  public void atualizar() {
    retornarMenu();
  }

  public void deletar() {
    retornarMenu();
  }

  public void retornarMenu() {
    Loteria.getMenuModalidades().init();
  }
}
