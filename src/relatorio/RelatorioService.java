package com.xpto.desafio.service;

import com.xpto.desafio.entity.Cliente;
import com.xpto.desafio.entity.Conta;
import com.xpto.desafio.entity.Movimentacao;
import com.xpto.desafio.entity.ReceitaXPTO;
import com.xpto.desafio.repository.ClienteRepository;
import com.xpto.desafio.repository.ContaRepository;
import com.xpto.desafio.repository.MovimentacaoRepository;
import com.xpto.desafio.repository.ReceitaXPTORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private ReceitaXPTORepository receitaXPTORepository;

    /**
     * Calcula a receita da XPTO para um cliente em um período de 30 dias.
     * Regra:
     * Até 10 movimentações: R$ 1,00 por movimentação
     * De 11 a 20 movimentações: R$ 0,75 por movimentação
     * Acima de 20 movimentações: R$ 0,50 por movimentação
     */
    @Transactional
    public ReceitaXPTO calcularReceitaXPTO(Long clienteId, LocalDate periodoInicio, LocalDate periodoFim) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));

        // 1. Obter todas as contas ativas do cliente
        List<Conta> contas = contaRepository.findByClienteIdAndExclusaoLogicaFalse(clienteId);

        // 2. Contar o total de movimentações no período
        long totalMovimentacoes = contas.stream()
                .flatMap(conta -> movimentacaoRepository.findByContaIdAndDataMovimentacaoBetween(
                        conta.getId(),
                        periodoInicio.atStartOfDay(),
                        periodoFim.atStartOfDay().plus(1, ChronoUnit.DAYS).minus(1, ChronoUnit.SECONDS)
                ).stream())
                .count();

        // 3. Aplicar a regra de precificação
        BigDecimal valorCobrado;
        if (totalMovimentacoes <= 10) {
            valorCobrado = BigDecimal.valueOf(totalMovimentacoes).multiply(new BigDecimal("1.00"));
        } else if (totalMovimentacoes <= 20) {
            // 10 * 1.00 + (totalMovimentacoes - 10) * 0.75
            valorCobrado = BigDecimal.valueOf(10).multiply(new BigDecimal("1.00"))
                    .add(BigDecimal.valueOf(totalMovimentacoes - 10).multiply(new BigDecimal("0.75")));
        } else {
            // 10 * 1.00 + 10 * 0.75 + (totalMovimentacoes - 20) * 0.50
            valorCobrado = BigDecimal.valueOf(10).multiply(new BigDecimal("1.00"))
                    .add(BigDecimal.valueOf(10).multiply(new BigDecimal("0.75")))
                    .add(BigDecimal.valueOf(totalMovimentacoes - 20).multiply(new BigDecimal("0.50")));
        }

        // 4. Salvar o registro da receita
        ReceitaXPTO receita = new ReceitaXPTO(
                cliente,
                periodoInicio,
                periodoFim,
                (int) totalMovimentacoes,
                valorCobrado.setScale(2, RoundingMode.HALF_UP)
        );

        return receitaXPTORepository.save(receita);
    }

    /**
     * Gera o Relatório de Saldo do Cliente X.
     */
    public String gerarRelatorioSaldoCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));

        // 1. Obter dados básicos
        String dataCadastro = cliente.getDataCadastro().toLocalDate().toString();
        Endereco endereco = cliente.getEnderecos().stream().findFirst().orElse(null); // Simplificação: pega o primeiro

        // 2. Calcular saldos e movimentações
        BigDecimal saldoInicialTotal = BigDecimal.ZERO;
        BigDecimal saldoAtualTotal = BigDecimal.ZERO;
        long creditos = 0;
        long debitos = 0;

        for (Conta conta : cliente.getContas()) {
            saldoInicialTotal = saldoInicialTotal.add(conta.getSaldoInicial());

            BigDecimal saldoConta = conta.getSaldoInicial();
            for (Movimentacao mov : conta.getMovimentacoes()) {
                if ("CREDITO".equals(mov.getTipo())) {
                    saldoConta = saldoConta.add(mov.getValor());
                    creditos++;
                } else if ("DEBITO".equals(mov.getTipo())) {
                    saldoConta = saldoConta.subtract(mov.getValor());
                    debitos++;
                }
            }
            saldoAtualTotal = saldoAtualTotal.add(saldoConta);
        }

        long totalMovimentacoes = creditos + debitos;

        // 3. Obter valor pago pelas movimentações (último registro de receita)
        BigDecimal valorPago = cliente.getReceitasXPTO().stream()
                .max((r1, r2) -> r1.getPeriodoFim().compareTo(r2.getPeriodoFim()))
                .map(ReceitaXPTO::getValorCobrado)
                .orElse(BigDecimal.ZERO);

        // 4. Formatar o relatório
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("--- Relatório de Saldo do Cliente ---\n");
        relatorio.append(String.format("Cliente: %s - Cliente desde: %s\n", cliente.getNome(), dataCadastro));
        if (endereco != null) {
            relatorio.append(String.format("Endereço: %s, %s, %s, %s, %s, %s\n",
                    endereco.getLogradouro(), endereco.getNumero(), endereco.getBairro(),
                    endereco.getCidade(), endereco.getUf(), endereco.getCep()));
        }
        relatorio.append(String.format("Movimentações de crédito: %d\n", creditos));
        relatorio.append(String.format("Movimentações de débito: %d\n", debitos));
        relatorio.append(String.format("Total de movimentações: %d\n", totalMovimentacoes));
        relatorio.append(String.format("Valor pago pelas movimentações: R$ %.2f\n", valorPago));
        relatorio.append(String.format("Saldo inicial: R$ %.2f\n", saldoInicialTotal));
        relatorio.append(String.format("Saldo atual: R$ %.2f\n", saldoAtualTotal));
        relatorio.append("--------------------------------------\n");

        return relatorio.toString();
    }

    /**
     * Gera o Relatório de Saldo do Cliente X e Período.
     */
    public String gerarRelatorioSaldoClientePorPeriodo(Long clienteId, LocalDate inicio, LocalDate fim) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));

        // 1. Obter dados básicos
        String dataCadastro = cliente.getDataCadastro().toLocalDate().toString();
        Endereco endereco = cliente.getEnderecos().stream().findFirst().orElse(null); // Simplificação: pega o primeiro

        // 2. Calcular saldos e movimentações no período
        long creditos = 0;
        long debitos = 0;
        BigDecimal saldoInicialPeriodo = BigDecimal.ZERO;
        BigDecimal saldoAtualPeriodo = BigDecimal.ZERO;

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.atStartOfDay().plus(1, ChronoUnit.DAYS).minus(1, ChronoUnit.SECONDS);

        for (Conta conta : cliente.getContas()) {
            // Saldo inicial do período: Saldo da conta na data de início
            BigDecimal saldoContaNoInicio = conta.getSaldoInicial();
            for (Movimentacao mov : conta.getMovimentacoes()) {
                if (mov.getDataMovimentacao().isBefore(inicioDateTime)) {
                    if ("CREDITO".equals(mov.getTipo())) {
                        saldoContaNoInicio = saldoContaNoInicio.add(mov.getValor());
                    } else if ("DEBITO".equals(mov.getTipo())) {
                        saldoContaNoInicio = saldoContaNoInicio.subtract(mov.getValor());
                    }
                }
            }
            saldoInicialPeriodo = saldoInicialPeriodo.add(saldoContaNoInicio);

            // Movimentações e saldo final no período
            BigDecimal saldoContaNoFim = saldoContaNoInicio;
            List<Movimentacao> movimentacoesPeriodo = movimentacaoRepository.findByContaIdAndDataMovimentacaoBetween(
                    conta.getId(), inicioDateTime, fimDateTime);

            for (Movimentacao mov : movimentacoesPeriodo) {
                if ("CREDITO".equals(mov.getTipo())) {
                    saldoContaNoFim = saldoContaNoFim.add(mov.getValor());
                    creditos++;
                } else if ("DEBITO".equals(mov.getTipo())) {
                    saldoContaNoFim = saldoContaNoFim.subtract(mov.getValor());
                    debitos++;
                }
            }
            saldoAtualPeriodo = saldoAtualPeriodo.add(saldoContaNoFim);
        }

        long totalMovimentacoes = creditos + debitos;

        // 3. Obter valor pago pelas movimentações (receita no período)
        BigDecimal valorPago = cliente.getReceitasXPTO().stream()
                .filter(r -> !r.getPeriodoInicio().isAfter(inicio) && !r.getPeriodoFim().isBefore(fim))
                .map(ReceitaXPTO::getValorCobrado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4. Formatar o relatório
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("--- Relatório de Saldo do Cliente por Período ---\n");
        relatorio.append(String.format("Período: %s a %s\n", inicio.toString(), fim.toString()));
        relatorio.append(String.format("Cliente: %s - Cliente desde: %s\n", cliente.getNome(), dataCadastro));
        if (endereco != null) {
            relatorio.append(String.format("Endereço: %s, %s, %s, %s, %s, %s\n",
                    endereco.getLogradouro(), endereco.getNumero(), endereco.getBairro(),
                    endereco.getCidade(), endereco.getUf(), endereco.getCep()));
        }
        relatorio.append(String.format("Movimentações de crédito: %d\n", creditos));
        relatorio.append(String.format("Movimentações de débito: %d\n", debitos));
        relatorio.append(String.format("Total de movimentações: %d\n", totalMovimentacoes));
        relatorio.append(String.format("Valor pago pelas movimentações (no período): R$ %.2f\n", valorPago));
        relatorio.append(String.format("Saldo inicial (no período): R$ %.2f\n", saldoInicialPeriodo));
        relatorio.append(String.format("Saldo atual (no período): R$ %.2f\n", saldoAtualPeriodo));
        relatorio.append("--------------------------------------------------\n");

        return relatorio.toString();
    }

    /**
     * Gera o Relatório de Saldo de Todos os Clientes.
     */
    public String gerarRelatorioSaldoTodosClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("--- Relatório de Saldo de Todos os Clientes ---\n");

        for (Cliente cliente : clientes) {
            BigDecimal saldoAtualTotal = BigDecimal.ZERO;
            for (Conta conta : cliente.getContas()) {
                BigDecimal saldoConta = conta.getSaldoInicial();
                for (Movimentacao mov : conta.getMovimentacoes()) {
                    if ("CREDITO".equals(mov.getTipo())) {
                        saldoConta = saldoConta.add(mov.getValor());
                    } else if ("DEBITO".equals(mov.getTipo())) {
                        saldoConta = saldoConta.subtract(mov.getValor());
                    }
                }
                saldoAtualTotal = saldoAtualTotal.add(saldoConta);
            }
            relatorio.append(String.format("Cliente: %s - Cliente desde: %s - Saldo em %s: R$ %.2f\n",
                    cliente.getNome(),
                    cliente.getDataCadastro().toLocalDate().toString(),
                    LocalDate.now().toString(),
                    saldoAtualTotal));
        }
        relatorio.append("--------------------------------------------------\n");
        return relatorio.toString();
    }

    /**
     * Gera o Relatório de Receita da Empresa (XPTO) por Período.
     */
    public String gerarRelatorioReceitaXPTO(LocalDate inicio, LocalDate fim) {
        List<ReceitaXPTO> receitas = receitaXPTORepository.findByPeriodoInicioBetween(inicio, fim);
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("--- Relatório de Receita da Empresa (XPTO) por Período ---\n");
        relatorio.append(String.format("Período: %s a %s\n", inicio.toString(), fim.toString()));

        BigDecimal totalReceitas = BigDecimal.ZERO;

        // Agrupar por cliente
        receitas.stream()
                .collect(Collectors.groupingBy(ReceitaXPTO::getCliente))
                .forEach((cliente, listaReceitas) -> {
                    long qtdMovimentacoes = listaReceitas.stream().mapToLong(ReceitaXPTO::getQtdMovimentacoes).sum();
                    BigDecimal valorTotalCobrado = listaReceitas.stream().map(ReceitaXPTO::getValorCobrado).reduce(BigDecimal.ZERO, BigDecimal::add);
                    totalReceitas = totalReceitas.add(valorTotalCobrado);

                    relatorio.append(String.format("Cliente %s - Quantidade de movimentações: %d - Valor das movimentações: R$ %.2f\n",
                            cliente.getNome(),
                            qtdMovimentacoes,
                            valorTotalCobrado));
                });

        relatorio.append(String.format("Total de receitas: R$ %.2f\n", totalReceitas));
        relatorio.append("------------------------------------------------------------\n");
        return relatorio.toString();
    }
}
