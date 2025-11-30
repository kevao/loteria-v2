package dev.loteria;

import dev.loteria.database.Conexao;
import dev.loteria.database.Migrations;
import dev.loteria.database.Seeds;
import dev.loteria.gui.MainApp;

/**
 * Classe principal da aplicação Loteria.
 * 
 * @author Kevin Villanova
 */
public final class Loteria {

  /**
   * Inicializa a aplicação.
   * 
   * @param args
   */
  public static void main(String[] args) {

    Conexao.getConn();
    Migrations.run();
    Seeds.run();
    MainApp.main(args);
  }

}
