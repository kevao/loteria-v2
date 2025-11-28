package dev.loteria;

import dev.loteria.database.Conexao;
import dev.loteria.ui.gui.MainApp;

/**
 * Classe principal — inicia a aplicação GUI.
 */
public final class Loteria {

  public static void main(String[] args) {
    // inicializa a conexão com o banco (DAOs usarão Conexao quando necessário)
    new Conexao();
    // inicia a aplicação JavaFX
    MainApp.main(args);
  }

}
