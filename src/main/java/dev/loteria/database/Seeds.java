package dev.loteria.database;

import dev.loteria.dao.ClienteDao;
import dev.loteria.dao.FuncionarioDao;
import dev.loteria.dao.JogoDao;
import dev.loteria.dao.ModalidadeDao;
import dev.loteria.models.Cliente;
import dev.loteria.models.Funcionario;
import dev.loteria.models.Jogo;
import dev.loteria.models.Modalidade;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Centraliza a inserção de dados iniciais (seeds) da aplicação. Insere
 * modalidades padrão, clientes, funcionários e jogos de exemplo quando as
 * respectivas tabelas estiverem vazias.
 */
public class Seeds {
  /**
   * Executa todos os seeds necessários. Seguro para chamar repetidamente —
   * cada bloco verifica se a tabela já contém registros antes de inserir.
   */
  public static void run() {
    try {
      ModalidadeDao mDao = new ModalidadeDao();
      ClienteDao cDao = new ClienteDao();
      FuncionarioDao fDao = new FuncionarioDao();
      JogoDao jDao = new JogoDao();
      // Seed modalidades (se não houver nenhuma)
      if (mDao.contar() == 0) {
        System.out.println("Seeds: inserindo modalidades padrão...");
        mDao.inserir(new Modalidade("Mega-Sena", 6, 1, 60, 6.0, "O jogo mais famoso do Brasil."));
        mDao.inserir(new Modalidade("Quina", 5, 1, 80, 4.5, "Jogo popular com sorteio de 5 números."));
        mDao.inserir(new Modalidade("Lotofácil", 15, 1, 25, 5.0, "Fácil de jogar, fácil de ganhar."));
        mDao.inserir(new Modalidade("Lotomania", 20, 0, 99, 2.5, "Jogo com 20 números sorteados entre 0 e 99."));
      }

      // Seed clientes (se não houver nenhum)
      if (cDao.contar() == 0) {
        System.out.println("Seeds: inserindo clientes padrão...");
        cDao.inserir(new Cliente("Zulmira Sampaio", "zulmira@example.com", "(11) 90000-0000",
            "Rua das Laranjeiras, 100", "111.111.111-11", true));
        cDao.inserir(new Cliente("Ariovaldo Guimarães", "ariovaldo@example.com", "(21) 90000-0001",
            "Av. das Palmeiras, 200", "222.222.222-22", true));
        cDao.inserir(new Cliente("Setembrina Cardoso", "setembrina@example.com", "(31) 90000-0002",
            "Praça da Sé, 50", "333.333.333-33", true));
      }

      // Seed funcionários (se não houver nenhum)
      if (fDao.contar() == 0) {
        System.out.println("Seeds: inserindo funcionários padrão...");
        fDao.inserir(new Funcionario("Péricles Josué Gusmão Pinheiro", "pericles@example.com", "(11) 91000-0000",
            "Av. Central, 1", "444.444.444-44", true, "ADM001", "Administração", 6000.0));
        fDao.inserir(new Funcionario("Quitéria Dantas Silvestre", "quiteria@example.com", "(21) 91000-0001",
            "Rua do Comércio, 45", "555.555.555-55", true, "VND001", "Vendas", 2800.0));
        fDao.inserir(new Funcionario("José Silvério Godoy", "jose@example.com", "(31) 91000-0002",
            "Rua da Segurança, 12", "666.666.666-66", true, "SEG001", "Segurança", 3000.0));
        fDao.inserir(new Funcionario("Marinalva Jesus da Silva", "marinalva@example.com", "(41) 91000-0003",
            "Praça do Serviço, 7", "777.777.777-77", true, "SRV001", "Serviços Gerais", 2300.0));
        fDao.inserir(new Funcionario("Raimundo Paulo Firmino Lopes", "raimundo@example.com", "(51) 91000-0004",
            "Av. dos Vendedores, 99", "888.888.888-88", true, "VND002", "Vendas", 2700.0));
      }

      // Agora cria alguns jogos de exemplo apenas se não houver nenhum registrado
      if (jDao.contar() == 0) {
        System.out.println("Seeds: criando jogos de exemplo...");
        ResultSet rms = null;
        ResultSet rcs = null;
        ResultSet rfs = null;
        try {
          rms = mDao.listar();
          rcs = cDao.listar();
          rfs = fDao.listar();

          if (rms.next() && rcs.next() && rfs.next()) {
            UUID mid = (UUID) rms.getObject("id");
            UUID cid = (UUID) rcs.getObject("id");
            UUID fid = (UUID) rfs.getObject("id");

            Modalidade m = mDao.getById(mid);
            Cliente c = cDao.getById(cid);
            Funcionario f = fDao.getById(fid);

            for (int i = 0; i < 3; i++) {
              Set<Integer> numeros = new LinkedHashSet<>();
              int tamanho = Math.max(2, m.getNumerosSorteio());
              int menor = m.getMenorBola();
              int maior = m.getMaiorBola();
              for (int n = 0; n < tamanho; n++) {
                numeros.add(menor + ((n + i) % (maior - menor + 1)));
              }
              Jogo jogo = new Jogo(m, c, f, numeros);
              jDao.inserir(jogo);
            }
          }
        } finally {
          try {
            if (rms != null)
              rms.close();
            if (rcs != null)
              rcs.close();
            if (rfs != null)
              rfs.close();
          } catch (SQLException ignore) {
          }
        }
      }

    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }
}
