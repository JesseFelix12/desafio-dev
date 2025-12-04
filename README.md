# desafio-dev
Este projeto foi desenvolvido como parte do Desafio Técnico DEV, cujo objetivo é demonstrar a capacidade de análise de requisitos, modelagem, desenvolvimento em Java, integração com banco de dados e aplicação de boas práticas de engenharia de software.

1. O sistema tem como finalidade realizar o controle de receitas e despesas de diversos clientes (Pessoa Física e Pessoa Jurídica) da empresa XPTO, bem como calcular a receita da própria empresa com base nas movimentações financeiras realizadas pelos clientes.

O foco principal deste projeto não está apenas na conclusão do sistema, mas principalmente na compreensão das regras de negócio, estruturação da solução e evolução do desenvolvimento.

2. Tecnologias Utilizadas

Java

Spring Boot

Maven

Banco de Dados Relacional

JPA / Hibernate

JUnit 5

Git e GitHub

VS Code / Eclipse (IDE de desenvolvimento)

Observação: O projeto foi estruturado de forma que possa ser facilmente integrado ao Oracle. Para fins de testes locais, foi utilizada configuração simplificada.

3. Objetivo do Sistema

O sistema tem como principais objetivos:

Controlar o cadastro de clientes (PF e PJ).

Controlar endereços, contas bancárias e movimentações financeiras.

Realizar o cálculo da receita da empresa XPTO com base na quantidade de movimentações.

Fornecer relatórios de saldo e de receita por período.

Demonstrar o uso de PL/SQL integrado ao Java.

Aplicar boas práticas de desenvolvimento e organização de código.

4. Regras de Negócio Implementadas
4.1 Tipos de Cliente

Pessoa Física (PF)

Pessoa Jurídica (PJ)

Foi realizada a normalização dos dados, separando atributos comuns e específicos de cada tipo.

4.2 Contas Bancárias

Um cliente pode possuir uma ou várias contas bancárias.

Todas as movimentações (débito e crédito) são realizadas por meio dessas contas.

Caso exista movimentação associada a uma conta:

Não é permitida a alteração física dos dados.

Apenas exclusão lógica é permitida.

4.3 Movimentações

Cada movimentação possui:

Tipo: Crédito ou Débito

Valor

Data

Conta associada

As movimentações simulam uma integração externa, realizada via carga de dados no sistema.

4.4 Receita da Empresa XPTO

A receita da empresa é calculada conforme a quantidade de movimentações em um período de 30 dias, a partir da data de cadastro do cliente:

Quantidade de Movimentações	Valor por Movimentação
Até 10	R$ 1,00
De 11 a 20	R$ 0,75
Acima de 20	R$ 0,50
5. Funcionalidades Desenvolvidas

✅ CRUD de Clientes

✅ CRUD de Endereços

✅ CRUD de Contas Bancárias

✅ Registro de Movimentações

✅ Relatório de Saldo por Cliente

✅ Relatório de Saldo por Período

✅ Relatório de Receita da Empresa XPTO por Período

✅ Cálculo automático de taxas por movimentação

✅ Classe de Testes Unitários

✅ Tratamento de Exceções

✅ Integração entre Java e Banco de Dados

✅ Versionamento no GitHub

6. Relatórios Gerados
6.1 Relatório de Saldo do Cliente

Cliente

Data de cadastro

Endereço

Total de créditos

Total de débitos

Total de movimentações

Valor pago pelas movimentações

Saldo inicial

Saldo atual

6.2 Relatório de Saldo por Período

Período informado

Dados completos do cliente

Quantidade de créditos e débitos

Valor pago no período

Saldo inicial

Saldo final

6.3 Relatório de Receita da Empresa (XPTO)

Período informado

Receita por cliente

Quantidade de movimentações

Total geral de receitas da empresa

7. Objeto PL/SQL

Foi implementado ao menos um objeto PL/SQL (Procedure / Function / Trigger) para atender ao requisito de integração direta entre o Java e o Banco de Dados Oracle, sendo o mesmo utilizado no fluxo de cadastro ou cálculo de dados.

8. Testes Unitários

Foram desenvolvidos testes unitários utilizando JUnit 5, com foco na validação da lógica de negócio das principais funcionalidades, especialmente:

Cadastro de clientes

Validação de movimentações

Cálculo de receitas

9. Boas Práticas Adotadas

Organização em camadas:

Controller

Service

Repository

Model

Separação de responsabilidades (SRP)

Uso de DTOs para comunicação entre camadas

Tratamento centralizado de exceções

Padronização de nomes de classes, métodos e variáveis

Versionamento contínuo com Git

Documentação clara no código

10. Padrões de Projeto Utilizados

MVC (Model-View-Controller)

Repository

Service Layer

DTO (Data Transfer Object)

11. Observações Importantes

Este projeto foi desenvolvido com foco na demonstração do raciocínio lógico, estruturação do sistema e entendimento das regras de negócio.

Nem todas as funcionalidades foram implementadas em sua totalidade conforme um sistema de produção, pois o principal objetivo do desafio é avaliar a evolução técnica do candidato.

Algumas configurações foram adaptadas para facilitar a execução em ambiente local.

12. Como Executar o Projeto

Clone o repositório:

git clone https://github.com/JesseFelix12/desafio-dev.git


Acesse a pasta do projeto:

cd desafio-dev


Execute a aplicação:

java -jar target/desafio-dev.jar


O sistema será iniciado em:

http://localhost:8080

**OBS: por limitações da minha maquina não consegui rodar o oracle, então fiz pelo H2 com instruções comentadas para rodar em um cenario que o oracle estaria disponivel.**
