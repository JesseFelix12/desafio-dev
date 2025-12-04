package com.xpto.desafio.service;

import com.xpto.desafio.entity.Cliente;
import com.xpto.desafio.entity.Conta;
import com.xpto.desafio.entity.Endereco;
import com.xpto.desafio.entity.Movimentacao;
import com.xpto.desafio.repository.ClienteRepository;
import com.xpto.desafio.repository.ContaRepository;
import com.xpto.desafio.repository.EnderecoRepository;
import com.xpto.desafio.repository.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    /**
     * Cria um novo cliente com movimentação inicial (transação atômica).
     * Simula a Stored Procedure SP_CRIAR_CLIENTE_INICIAL do Oracle.
     */
    @Transactional
    public Cliente criarClienteComMovimentacaoInicial(
            String nome, String tipoPessoa, String cpf, String cnpj, String telefone,
            String logradouro, String numero, String bairro, String cidade, String uf, String cep,
            String numeroConta, String agencia, String instituicaoFinanceira, BigDecimal saldoInicial) {

        // 1. Criar e salvar o cliente
        Cliente cliente = new Cliente(nome, tipoPessoa, cpf, cnpj, telefone);
        cliente = clienteRepository.save(cliente);

        // 2. Criar e salvar o endereço
        Endereco endereco = new Endereco(cliente, logradouro, numero, bairro, cidade, uf, cep);
        enderecoRepository.save(endereco);

        // 3. Criar e salvar a conta
        Conta conta = new Conta(cliente, numeroConta, agencia, instituicaoFinanceira, saldoInicial);
        conta = contaRepository.save(conta);

        // 4. Criar e salvar a movimentação inicial (saldo inicial)
        Movimentacao movimentacao = new Movimentacao(conta, "CREDITO", saldoInicial, "Saldo Inicial da Conta");
        movimentacaoRepository.save(movimentacao);

        return cliente;
    }

    /**
     * Obtém um cliente pelo ID.
     */
    public Optional<Cliente> obterClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    /**
     * Obtém todos os clientes.
     */
    public List<Cliente> obterTodosClientes() {
        return clienteRepository.findAll();
    }

    /**
     * Atualiza um cliente (com restrições).
     * Nota: Campos como CPF, CNPJ e dataCadastro não podem ser alterados para manter histórico.
     */
    @Transactional
    public Cliente atualizarCliente(Long id, String nome, String telefone) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            cliente.setNome(nome);
            cliente.setTelefone(telefone);
            return clienteRepository.save(cliente);
        }
        throw new IllegalArgumentException("Cliente não encontrado com ID: " + id);
    }

    /**
     * Deleta um cliente (exclusão física).
     * Nota: Considerar exclusão lógica se houver movimentações.
     */
    @Transactional
    public void deletarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    /**
     * Obtém um cliente pelo CPF.
     */
    public Optional<Cliente> obterClientePorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }

    /**
     * Obtém um cliente pelo CNPJ.
     */
    public Optional<Cliente> obterClientePorCnpj(String cnpj) {
        return clienteRepository.findByCnpj(cnpj);
    }

    /**
     * Obtém todos os clientes de um tipo específico (PF ou PJ).
     */
    public List<Cliente> obterClientesPorTipo(String tipoPessoa) {
        return clienteRepository.findByTipoPessoa(tipoPessoa);
    }
}
