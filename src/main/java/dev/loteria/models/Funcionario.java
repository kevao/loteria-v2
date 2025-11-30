package dev.loteria.models;

/**
 * Representa um funcionário do sistema. Extende {@link Pessoa} e adiciona
 * campos específicos como matrícula, cargo e salário.
 *
 * Instâncias são manipuladas por {@code FuncionarioDao} e expostas à camada
 * de serviços para operações de gestão de pessoal.
 */
public class Funcionario extends Pessoa {
  private String matricula;
  private String cargo;
  private double salario;

  public Funcionario() {
    super();
  }

  /**
   * Construtor vazio.
   */

  /**
   * Cria um novo funcionário (sem `id`).
   *
   * @param nome      nome completo
   * @param email     e-mail de contato
   * @param telefone  telefone de contato
   * @param endereco  endereço completo
   * @param cpf       CPF do funcionário
   * @param ativo     flag de ativo/inativo
   * @param matricula matrícula funcional
   * @param cargo     cargo/função
   * @param salario   salário base
   */
  public Funcionario(String nome, String email, String telefone, String endereco, String cpf, boolean ativo,
      String matricula, String cargo, double salario) {
    super(nome, email, telefone, endereco, cpf, ativo);
    this.matricula = matricula;
    this.cargo = cargo;
    this.salario = salario;
  }

  /**
   * Construtor usado quando o `id` do funcionário já existe (ex.: leitura do DB).
   *
   * @param id        UUID do funcionário
   * @param nome      nome completo
   * @param email     e-mail
   * @param telefone  telefone
   * @param endereco  endereço
   * @param cpf       CPF
   * @param ativo     flag de ativo
   * @param matricula matrícula
   * @param cargo     cargo
   * @param salario   salário
   */
  public Funcionario(java.util.UUID id, String nome, String email, String telefone, String endereco, String cpf,
      boolean ativo, String matricula, String cargo, double salario) {
    super(id, nome, email, telefone);
    setEndereco(endereco);
    setCpf(cpf);
    setAtivo(ativo);
    this.matricula = matricula;
    this.cargo = cargo;
    this.salario = salario;
  }

  public String getMatricula() {
    return matricula;
  }

  /**
   * Retorna a matrícula do funcionário.
   *
   * @return matrícula
   */

  public void setMatricula(String matricula) {
    this.matricula = matricula;
  }

  /**
   * Define a matrícula do funcionário.
   *
   * @param matricula nova matrícula
   */

  public String getCargo() {
    return cargo;
  }

  /**
   * Retorna o cargo do funcionário.
   *
   * @return cargo
   */

  public void setCargo(String cargo) {
    this.cargo = cargo;
  }

  /**
   * Define o cargo do funcionário.
   *
   * @param cargo novo cargo
   */

  public double getSalario() {
    return salario;
  }

  /**
   * Retorna o salário do funcionário.
   *
   * @return salário
   */

  public void setSalario(double salario) {
    this.salario = salario;
  }

  /**
   * Define o salário do funcionário.
   *
   * @param salario novo salário
   */

  @Override
  public String toString() {
    return "Funcionario{" + "id=" + getId() + ", nome='" + getNome() + '\'' + ", email='" + getEmail() + '\''
        + ", telefone='" + getTelefone() + '\'' + ", matricula='" + matricula + '\'' + ", cargo='" + cargo + '\''
        + ", salario=" + salario + ", endereco='" + getEndereco() + '\'' + ", cpf='" + getCpf() + '\'' + ", ativo="
        + isAtivo() + '}';
  }
}
