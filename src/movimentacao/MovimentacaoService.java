package com.xpto.desafio.service;

import com.xpto.desafio.entity.Conta;
import com.xpto.desafio.entity.Movimentacao;
import com.xpto.desafio.repository.ContaRepository;
import com.xpto.desafio.repository.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private ContaRepository contaRepository;

    /**
     * Simula a integração: recebe a movimentação e efetua o cadastro.
     */
    @Transactional
    public Movimentacao receberEfetuarMovimentacao(Long contaId, String tipo, String descricao, double valor) {
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada."));

        // Simulação de identificação do cliente já feita pelo relacionamento da Conta

        Movimentacao movimentacao = new Movimentacao(conta, tipo, new java.math.BigDecimal(valor), descricao);
        return movimentacaoRepository.save(movimentacao);
    }

    /**
     * Obtém todas as movimentações de uma conta.
     */
    public List<Movimentacao> obterMovimentacoesPorConta(Long contaId) {
        return movimentacaoRepository.findByContaId(contaId);
    }

    /**
     * Obtém movimentações por conta e período.
     */
    public List<Movimentacao> obterMovimentacoesPorContaEPeriodo(Long contaId, LocalDateTime inicio, LocalDateTime fim) {
        return movimentacaoRepository.findByContaIdAndDataMovimentacaoBetween(contaId, inicio, fim);
    }

    /**
     * CRUD: Obter movimentação por ID.
     */
    public Optional<Movimentacao> obterMovimentacaoPorId(Long id) {
        return movimentacaoRepository.findById(id);
    }

    /**
     * CRUD: Deletar movimentação.
     */
    @Transactional
    public void deletarMovimentacao(Long id) {
        movimentacaoRepository.deleteById(id);
    }
}
