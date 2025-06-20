package dev.loteria.interfaces;

import java.sql.ResultSet;

public interface CRUD<T> {
  public ResultSet listar();

  public void inserir(T modelo);

  public void editar(T modelo);

  public void deletar(int id);
}
