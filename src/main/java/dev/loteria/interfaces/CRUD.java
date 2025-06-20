package dev.loteria.interfaces;

import java.sql.ResultSet;

public interface CRUD<TModelo> {
  public ResultSet listar();

  public void inserir(TModelo modelo);

  public void editar(TModelo modelo);

  public void deletar(int id);
}
