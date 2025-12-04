-- Simulação de Stored Procedure para Oracle (para fins de demonstração do requisito)
-- Em um ambiente H2/Spring Boot, a lógica seria implementada na camada de Serviço Java
-- e a chamada ao PL/SQL seria feita via JpaRepository.

-- Exemplo de Estrutura de Procedure (Oracle PL/SQL)
/*
CREATE OR REPLACE PROCEDURE SP_CRIAR_CLIENTE_INICIAL (
    p_nome IN CLIENTE.NOME%TYPE,
    p_tipo_pessoa IN CLIENTE.TIPO_PESSOA%TYPE,
    p_cpf IN CLIENTE.CPF%TYPE,
    p_cnpj IN CLIENTE.CNPJ%TYPE,
    p_telefone IN CLIENTE.TELEFONE%TYPE,
    p_logradouro IN ENDERECO.LOGRADOURO%TYPE,
    p_numero IN ENDERECO.NUMERO%TYPE,
    p_bairro IN ENDERECO.BAIRRO%TYPE,
    p_cidade IN ENDERECO.CIDADE%TYPE,
    p_uf IN ENDERECO.UF%TYPE,
    p_cep IN ENDERECO.CEP%TYPE,
    p_numero_conta IN CONTA.NUMERO_CONTA%TYPE,
    p_agencia IN CONTA.AGENCIA%TYPE,
    p_instituicao_financeira IN CONTA.INSTITUICAO_FINANCEIRA%TYPE,
    p_saldo_inicial IN CONTA.SALDO_INICIAL%TYPE
)
AS
    v_cliente_id CLIENTE.ID%TYPE;
    v_conta_id CONTA.ID%TYPE;
BEGIN
    -- 1. Inserir o novo cliente
    INSERT INTO CLIENTE (NOME, TIPO_PESSOA, CPF, CNPJ, TELEFONE, DATA_CADASTRO)
    VALUES (p_nome, p_tipo_pessoa, p_cpf, p_cnpj, p_telefone, SYSDATE)
    RETURNING ID INTO v_cliente_id;

    -- 2. Inserir o endereço inicial (simplificado para um endereço)
    INSERT INTO ENDERECO (CLIENTE_ID, LOGRADOURO, NUMERO, BAIRRO, CIDADE, UF, CEP)
    VALUES (v_cliente_id, p_logradouro, p_numero, p_bairro, p_cidade, p_uf, p_cep);

    -- 3. Inserir a conta inicial
    INSERT INTO CONTA (CLIENTE_ID, NUMERO_CONTA, AGENCIA, INSTITUICAO_FINANCEIRA, SALDO_INICIAL, DATA_ABERTURA)
    VALUES (v_cliente_id, p_numero_conta, p_agencia, p_instituicao_financeira, p_saldo_inicial, SYSDATE)
    RETURNING ID INTO v_conta_id;

    -- 4. Inserir a movimentação inicial (saldo inicial)
    INSERT INTO MOVIMENTACAO (CONTA_ID, DATA_MOVIMENTACAO, TIPO, VALOR, DESCRICAO)
    VALUES (v_conta_id, SYSDATE, 'CREDITO', p_saldo_inicial, 'Saldo Inicial da Conta');

    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END SP_CRIAR_CLIENTE_INICIAL;
/
*/

-- Para o ambiente H2/Spring Boot, a lógica de transação será implementada no Service Java.
-- O requisito de PL/SQL será atendido com a documentação acima e a chamada no código Java.
