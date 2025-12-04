package com.xpto.desafio.controller;

import com.xpto.desafio.entity.Cliente;
import com.xpto.desafio.entity.Endereco;
import com.xpto.desafio.service.ClienteService;
import com.xpto.desafio.service.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EnderecoService enderecoService;

    // --- CRUD Cliente ---

    @PostMapping
    public ResponseEntity<Cliente> criarCliente(
            @RequestParam String nome,
            @RequestParam String tipoPessoa,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String cnpj,
            @RequestParam(required = false) String telefone,
            @RequestParam String logradouro,
            @RequestParam(required = false) String numero,
            @RequestParam String bairro,
            @RequestParam String cidade,
            @RequestParam String uf,
            @RequestParam String cep,
            @RequestParam String numeroConta,
            @RequestParam String agencia,
            @RequestParam String instituicaoFinanceira,
            @RequestParam BigDecimal saldoInicial) {
        try {
            Cliente novoCliente = clienteService.criarClienteComMovimentacaoInicial(
                    nome, tipoPessoa, cpf, cnpj, telefone,
                    logradouro, numero, bairro, cidade, uf, cep,
                    numeroConta, agencia, instituicaoFinanceira, saldoInicial);
            return new ResponseEntity<>(novoCliente, HttpStatus.CREATED);
        } catch (Exception e) {
            // Tratar exceções de validação ou banco de dados
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obterCliente(@PathVariable Long id) {
        return clienteService.obterClientePorId(id)
                .map(cliente -> new ResponseEntity<>(cliente, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> obterTodosClientes() {
        return new ResponseEntity<>(clienteService.obterTodosClientes(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarCliente(
            @PathVariable Long id,
            @RequestParam String nome,
            @RequestParam(required = false) String telefone) {
        try {
            Cliente clienteAtualizado = clienteService.atualizarCliente(id, nome, telefone);
            return new ResponseEntity<>(clienteAtualizado, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        try {
            clienteService.deletarCliente(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --- CRUD Endereço ---

    @GetMapping("/{clienteId}/enderecos")
    public ResponseEntity<List<Endereco>> obterEnderecosPorCliente(@PathVariable Long clienteId) {
        return new ResponseEntity<>(enderecoService.obterEnderecosPorCliente(clienteId), HttpStatus.OK);
    }

    @PostMapping("/{clienteId}/enderecos")
    public ResponseEntity<Endereco> criarEndereco(@PathVariable Long clienteId, @RequestBody Endereco endereco) {
        return clienteService.obterClientePorId(clienteId)
                .map(cliente -> {
                    endereco.setCliente(cliente);
                    return new ResponseEntity<>(enderecoService.criarEndereco(endereco), HttpStatus.CREATED);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/enderecos/{id}")
    public ResponseEntity<Endereco> atualizarEndereco(@PathVariable Long id, @RequestBody Endereco endereco) {
        try {
            Endereco enderecoAtualizado = enderecoService.atualizarEndereco(id, endereco);
            return new ResponseEntity<>(enderecoAtualizado, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/enderecos/{id}")
    public ResponseEntity<Void> deletarEndereco(@PathVariable Long id) {
        try {
            enderecoService.deletarEndereco(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
