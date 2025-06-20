package dev.loteria.services;

import java.sql.ResultSet;

import de.vandermeer.asciitable.AsciiTable;
import dev.loteria.Loteria;
import dev.loteria.dao.ModalidadeDao;
import dev.loteria.interfaces.Servico;
import dev.loteria.models.Modalidade;

public class SorteioService implements Servico {

  ModalidadeDao modalidadeDao;

  public SorteioService() {
    modalidadeDao = new ModalidadeDao();
  }

  public void listar() {
    ResultSet rs = modalidadeDao.listar();
    AsciiTable at = new AsciiTable();

    String[] colunas = { "ID", "Nome", "Números no Sorteio", "Menor Bola", "Maior Bola", "Valor do Jogo", "Descrição" };

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
      System.out.println("Erro ao listar modalidades.");
    }

    System.out.println(at.render());

    System.out.print("Aperte Enter para retornar ao menu: ");
    System.console().readLine();
    retornarMenu();
  }

  public void inserir() {
    try {
      System.out.print("Nome da modalidade: ");
      String nome = System.console().readLine();

      System.out.print("Quantidade de números no sorteio: ");
      int numerosSorteio = Integer.parseInt(System.console().readLine());

      System.out.print("Menor número possível: ");
      int menorBola = Integer.parseInt(System.console().readLine());

      System.out.print("Maior número possível: ");
      int maiorBola = Integer.parseInt(System.console().readLine());

      System.out.print("Valor do jogo: ");
      double valorJogo = Double.parseDouble(System.console().readLine());

      System.out.print("Descrição: ");
      String descricao = System.console().readLine();

      Modalidade modalidade = new Modalidade(nome, numerosSorteio, menorBola, maiorBola, valorJogo, descricao);
      modalidadeDao.inserir(modalidade);
    } catch (Exception e) {
      System.out.println("Erro ao inserir modalidade. Verifique os dados informados.");
    }
    retornarMenu();
  }

  public void editar() {
    try {
      System.out.print("ID da modalidade a ser editada: ");
      int id = Integer.parseInt(System.console().readLine());

      System.out.print("Novo nome da modalidade: ");
      String nome = System.console().readLine();

      System.out.print("Nova quantidade de números no sorteio: ");
      int numerosSorteio = Integer.parseInt(System.console().readLine());

      System.out.print("Novo menor número possível: ");
      int menorBola = Integer.parseInt(System.console().readLine());

      System.out.print("Novo maior número possível: ");
      int maiorBola = Integer.parseInt(System.console().readLine());

      System.out.print("Novo valor do jogo: ");
      double valorJogo = Double.parseDouble(System.console().readLine());

      System.out.print("Nova descrição: ");
      String descricao = System.console().readLine();

      Modalidade modalidade = new Modalidade(id, nome, numerosSorteio, menorBola, maiorBola, valorJogo, descricao);
      modalidadeDao.editar(modalidade);
    } catch (Exception e) {
      System.out.println("Erro ao editar modalidade. Verifique os dados informados.");
    }
    retornarMenu();
  }

  public void deletar() {
    try {
      System.out.print("ID da modalidade a ser deletada: ");
      int id = Integer.parseInt(System.console().readLine());
      modalidadeDao.deletar(id);
      System.out.println("Modalidade deletada com sucesso!");
    } catch (Exception e) {
      System.out.println("Erro ao deletar modalidade. Verifique o ID informado.");
    }
    retornarMenu();
  }

  public void retornarMenu() {
    Loteria.getMenuModalidades().init();
  }
}
