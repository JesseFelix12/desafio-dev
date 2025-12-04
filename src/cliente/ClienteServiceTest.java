package com.xpto.desafio.service;

import com.xpto.desafio.entity.Cliente;
import com.xpto.desafio.repository.ClienteRepository;
import com.xpto.desafio.repository.ContaRepository;
import com.xpto.desafio.repository.EnderecoRepository;
import com.xpto.desafio.repository.MovimentacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private MovimentacaoRepository movimentacaoRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente clientePF;

    @BeforeEach
    void setUp() {
        clientePF = new Cliente("João da Silva", "PF", "123.456.789-00", null, "11999999999");
        clientePF.setId(1L);
    }

    @Test
    void deveCriarClienteComMovimentacaoInicial() {
        // Mocking
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clientePF);
        when(contaRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(enderecoRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(movimentacaoRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Execução
        Cliente novoCliente = clienteService.criarClienteComMovimentacaoInicial(
                "João da Silva", "PF", "123.456.789-00", null, "11999999999",
                "Rua A", "100", "Bairro B", "Cidade C", "SP", "01000-000",
                "12345-6", "0001", "Banco X", new BigDecimal("1000.00"));

        // Verificação
        assertNotNull(novoCliente);
        assertEquals("João da Silva", novoCliente.getNome());
        assertEquals("PF", novoCliente.getTipoPessoa());
    }

    @Test
    void deveAtualizarCliente() {
        // Mocking
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clientePF));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(i -> i.getArguments()[0]);

        // Execução
        Cliente clienteAtualizado = clienteService.atualizarCliente(1L, "João da Silva Atualizado", "11888888888");

        // Verificação
        assertEquals("João da Silva Atualizado", clienteAtualizado.getNome());
        assertEquals("11888888888", clienteAtualizado.getTelefone());
    }

    @Test
    void deveLancarExcecaoAoAtualizarClienteInexistente() {
        // Mocking
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        // Execução e Verificação
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.atualizarCliente(99L, "Nome", "Telefone");
        });
    }
}
