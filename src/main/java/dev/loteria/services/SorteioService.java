package dev.loteria.services;

import java.sql.ResultSet;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import dev.loteria.dao.ModalidadeDao;
import dev.loteria.dao.SorteioDao;
import dev.loteria.interfaces.Servico;
import dev.loteria.models.Modalidade;
import dev.loteria.models.Sorteio;

public class SorteioService implements Servico {

  SorteioDao sorteioDao;
  ModalidadeDao modalidadeDao = new ModalidadeDao();

  public SorteioService() {
    sorteioDao = new SorteioDao();
  }

  public void listar() {
    ResultSet rs = sorteioDao.listar();
    AsciiTable at = new AsciiTable();

    String[] colunas = { "ID", "Modalidade", "Números", "Data/Hora" };

    at.addRule();
    at.addRow((Object[]) colunas);
    at.addRule();

    try {
      while (rs != null && rs.next()) {
        String[] linha = new String[colunas.length];
        for (int i = 0; i < colunas.length; i++) {
          linha[i] = rs.getString(i + 1);
        }
        at.addRow((Object[]) linha);
        at.addRule();
      }
      if (rs != null) {
        rs.close();
      }
    } catch (Exception e) {
      System.out.println("Erro ao listar sorteios.");
    }

    CWC_LongestLine larguraColunas = new CWC_LongestLine();

    at.getRenderer().setCWC(larguraColunas);

    System.out.println(at.render());

    System.out.print("Aperte Enter para retornar ao menu: ");
    System.console().readLine();
    retornarMenu();
  }

  public void inserir() {
    try {
      System.out.print("ID da modalidade para o sorteio: ");
      java.util.UUID modalidadeId = java.util.UUID.fromString(System.console().readLine());

      Modalidade modalidade = modalidadeDao.getById(modalidadeId);
      if (modalidade == null) {
        System.out.println("Modalidade não encontrada.");
        retornarMenu();
        return;
      }

      Sorteio sorteio = new Sorteio(modalidade);
      sorteioDao.inserir(sorteio);
    } catch (Exception e) {
      System.out.println("Erro ao inserir sorteio. Verifique os dados informados.");
    }
    retornarMenu();
  }

  public void editar() {
    System.out.println("Não é possível editar um sorteio.");
    retornarMenu();
  }

  public void deletar() {
    try {
      System.out.print("ID do sorteio a ser deletado: ");
      java.util.UUID id = java.util.UUID.fromString(System.console().readLine());
      sorteioDao.deletar(id);
    } catch (Exception e) {
      System.out.println("Erro ao deletar sorteio. Verifique o ID informado.");
    }
    retornarMenu();
  }

  public void retornarMenu() {
    // Console UI removed. GUI handles navigation — no-op here.
  }
}
