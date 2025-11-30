package dev.loteria.models;

/**
 * Representa um cliente do sistema. Extende {@link Pessoa} e agrega
 * atributos inerentes ao cliente (endereço, CPF e sinalizador de ativo).
 *
 * Usado pelo DAO de clientes e pela camada de serviços para operações de
 * CRUD e apresentação.
 */
public class Cliente extends Pessoa {

  public Cliente() {
    super();
  }

  /**
   * Construtor vazio usado por frameworks ou mapeadores.
   */

  /**
   * Construtor para criar um novo cliente (sem `id`).
   *
   * @param nome     nome completo
   * @param email    endereço de e-mail
   * @param telefone telefone de contato
   * @param endereco endereço completo
   * @param cpf      CPF do cliente
   * @param ativo    flag indicando se o cliente está ativo
   */
  public Cliente(String nome, String email, String telefone, String endereco, String cpf, boolean ativo) {
    super(nome, email, telefone, endereco, cpf, ativo);
  }

  /**
   * Construtor usado quando o `id` já foi gerado (por exemplo, ao recuperar
   * do banco).
   *
   * @param id       UUID do cliente
   * @param nome     nome completo
   * @param email    endereço de e-mail
   * @param telefone telefone de contato
   * @param endereco endereço completo
   * @param cpf      CPF do cliente
   * @param ativo    flag indicando se o cliente está ativo
   */
  public Cliente(java.util.UUID id, String nome, String email, String telefone, String endereco, String cpf,
      boolean ativo) {
    super(id, nome, email, telefone);
    setEndereco(endereco);
    setCpf(cpf);
    setAtivo(ativo);
  }

  @Override
  public String toString() {
    return "Cliente{" + "id=" + getId() + ", nome='" + getNome() + '\'' + ", email='" + getEmail() + '\''
        + ", telefone='" + getTelefone() + '\'' + ", endereco='" + getEndereco() + '\'' + ", ativo=" + isAtivo() + '}';
  }
}
