package com.xpto.desafio.controller;

import com.xpto.desafio.entity.Conta;
import com.xpto.desafio.entity.Movimentacao;
import com.xpto.desafio.service.ContaService;
import com.xpto.desafio.service.MovimentacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @Autowired
    private MovimentacaoService movimentacaoService;

    // --- CRUD Conta ---

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Conta>> obterContasPorCliente(@PathVariable Long clienteId) {
        return new ResponseEntity<>(contaService.obterContasPorCliente(clienteId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Conta> criarConta(@RequestBody Conta conta) {
        return new ResponseEntity<>(contaService.criarConta(conta), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Conta> atualizarConta(
            @PathVariable Long id,
            @RequestParam String numeroConta,
            @RequestParam String agencia,
            @RequestParam String instituicaoFinanceira) {
        try {
            Conta contaAtualizada = contaService.atualizarConta(id, numeroConta, agencia, instituicaoFinanceira);
            return new ResponseEntity<>(contaAtualizada, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Conta com movimentação
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarConta(@PathVariable Long id) {
        try {
            contaService.deletarConta(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --- Movimentação (Simulação de Integração) ---

    @PostMapping("/{contaId}/movimentacao")
    public ResponseEntity<Movimentacao> receberMovimentacao(
            @PathVariable Long contaId,
            @RequestParam String tipo, // CREDITO ou DEBITO
            @RequestParam String descricao,
            @RequestParam double valor) {
        try {
            Movimentacao movimentacao = movimentacaoService.receberEfetuarMovimentacao(contaId, tipo, descricao, valor);
            return new ResponseEntity<>(movimentacao, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{contaId}/movimentacoes")
    public ResponseEntity<List<Movimentacao>> obterMovimentacoesPorConta(@PathVariable Long contaId) {
        return new ResponseEntity<>(movimentacaoService.obterMovimentacoesPorConta(contaId), HttpStatus.OK);
    }
}
