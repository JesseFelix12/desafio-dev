package com.xpto.desafio.service;

import com.xpto.desafio.entity.Conta;
import com.xpto.desafio.entity.Movimentacao;
import com.xpto.desafio.repository.ContaRepository;
import com.xpto.desafio.repository.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    /**
     * Obtém uma conta pelo ID.
     */
    public Optional<Conta> obterContaPorId(Long id) {
        return contaRepository.findById(id);
    }

    /**
     * Obtém todas as contas de um cliente.
     */
    public List<Conta> obterContasPorCliente(Long clienteId) {
        return contaRepository.findByClienteId(clienteId);
    }

    /**
     * Obtém todas as contas ativas (não excluídas logicamente) de um cliente.
     */
    public List<Conta> obterContasAtivasPorCliente(Long clienteId) {
        return contaRepository.findByClienteIdAndExclusaoLogicaFalse(clienteId);
    }

    /**
     * Cria uma nova conta.
     */
    @Transactional
    public Conta criarConta(Conta conta) {
        return contaRepository.save(conta);
    }

    /**
     * Atualiza uma conta (com restrições).
     * Se houver movimentações associadas, a alteração é bloqueada.
     * Apenas exclusão lógica é permitida.
     */
    @Transactional
    public Conta atualizarConta(Long id, String numeroConta, String agencia, String instituicaoFinanceira) {
        Optional<Conta> contaOpt = contaRepository.findById(id);
        if (contaOpt.isPresent()) {
            Conta conta = contaOpt.get();

            // Verificar se há movimentações
            List<Movimentacao> movimentacoes = movimentacaoRepository.findByContaId(id);
            if (!movimentacoes.isEmpty()) {
                throw new IllegalStateException("Não é permitido alterar uma conta com movimentações associadas. Use exclusão lógica.");
            }

            conta.setNumeroConta(numeroConta);
            conta.setAgencia(agencia);
            conta.setInstituicaoFinanceira(instituicaoFinanceira);
            return contaRepository.save(conta);
        }
        throw new IllegalArgumentException("Conta não encontrada com ID: " + id);
    }

    /**
     * Realiza exclusão lógica de uma conta.
     * Se não houver movimentações, permite exclusão física.
     */
    @Transactional
    public void deletarConta(Long id) {
        Optional<Conta> contaOpt = contaRepository.findById(id);
        if (contaOpt.isPresent()) {
            Conta conta = contaOpt.get();
            List<Movimentacao> movimentacoes = movimentacaoRepository.findByContaId(id);

            if (!movimentacoes.isEmpty()) {
                // Exclusão lógica
                conta.setExclusaoLogica(true);
                contaRepository.save(conta);
            } else {
                // Exclusão física
                contaRepository.deleteById(id);
            }
        }
    }

    /**
     * Verifica se uma conta possui movimentações.
     */
    public boolean temMovimentacoes(Long contaId) {
        List<Movimentacao> movimentacoes = movimentacaoRepository.findByContaId(contaId);
        return !movimentacoes.isEmpty();
    }
}
