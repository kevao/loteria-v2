# LOTERIA

Projeto de Loteria (Java, MySQL) para a UNCISAL na disciplina de Programação Orientada a Objetos I.

## Descrição
Este projeto simula um sistema de loteria, permitindo o cadastro de modalidades, realização de sorteios e gerenciamento dos dados via interface de console. Utiliza arquitetura orientada a objetos e persistência em banco de dados MySQL.

## Funcionalidades
- **Cadastro de Modalidades:**
	- Adicione, edite, liste e remova diferentes modalidades de loteria, cada uma com regras próprias (quantidade de números, faixa de bolas, valor do jogo, descrição).
- **Sorteios:**
	- Realize sorteios automáticos para cada modalidade cadastrada, gerando números aleatórios conforme as regras da modalidade.
	- Liste e remova sorteios realizados.
- **Persistência:**
	- Todos os dados são salvos em um banco MySQL, garantindo integridade e consulta posterior.
- **Interface de Console:**
	- Menus interativos para navegação entre funcionalidades principais: Modalidades, Sorteios e Sair.

## Estrutura do Projeto
```
src/main/java/dev/loteria/
├── Loteria.java           # Classe principal, inicializa menus e conexão
├── database/Conexao.java  # Gerencia conexão com MySQL
├── dao/                   # Acesso a dados (ModalidadeDao, SorteioDao)
├── interfaces/            # Interfaces de abstração (CRUD, Modelo, Servico)
├── models/                # Modelos de dados (Modalidade, Sorteio)
├── services/              # Lógica de negócio (ModalidadeService, SorteioService)
├── ui/                    # Menus de interação (MenuPrincipal, MenuModalidades, MenuSorteios)
```

## Banco de Dados
- **MySQL**
	- Tabelas para modalidades e sorteios.
	- Configuração de acesso em `Conexao.java`.

## Como Executar
1. Configure o MySQL local e crie o banco `loteria`.
2. Ajuste usuário/senha em `Conexao.java` se necessário.
3. Compile o projeto:
	 ```bash
	 javac -d target/classes src/main/java/dev/loteria/**/*.java
	 ```
4. Execute:
	 ```bash
	 java -cp target/classes dev.loteria.Loteria
	 ```
