package dev.loteria.services;

import dev.loteria.Loteria;
import dev.loteria.dao.ModalidadeDao;
import dev.loteria.interfaces.Servico;

public class ModalidadeService implements Servico {

  ModalidadeDao modalidadeDao;

  public ModalidadeService() {
    modalidadeDao = new ModalidadeDao();
  }

  public void listar() {
  }

  public void inserir() {
    retornarMenu();
  }

  public void editar() {
    retornarMenu();
  }

  public void deletar() {
    retornarMenu();
  }

  public void retornarMenu() {
    Loteria.getMenuModalidades().init();
  }
}
