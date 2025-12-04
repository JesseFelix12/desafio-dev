package com.xpto.desafio.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "CONTA")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENTE_ID", nullable = false)
    private Cliente cliente;

    @Column(name = "NUMERO_CONTA", nullable = false)
    private String numeroConta;

    @Column(name = "AGENCIA", nullable = false)
    private String agencia;

    @Column(name = "INSTITUICAO_FINANCEIRA", nullable = false)
    private String instituicaoFinanceira;

    @Column(name = "SALDO_INICIAL", nullable = false)
    private BigDecimal saldoInicial;

    @Column(name = "DATA_ABERTURA", nullable = false)
    private LocalDateTime dataAbertura;

    @Column(name = "EXCLUSAO_LOGICA", nullable = false)
    private Boolean exclusaoLogica = false;

    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Movimentacao> movimentacoes;

    // Construtores
    public Conta() {
    }

    public Conta(Cliente cliente, String numeroConta, String agencia, 
                 String instituicaoFinanceira, BigDecimal saldoInicial) {
        this.cliente = cliente;
        this.numeroConta = numeroConta;
        this.agencia = agencia;
        this.instituicaoFinanceira = instituicaoFinanceira;
        this.saldoInicial = saldoInicial;
        this.dataAbertura = LocalDateTime.now();
        this.exclusaoLogica = false;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getInstituicaoFinanceira() {
        return instituicaoFinanceira;
    }

    public void setInstituicaoFinanceira(String instituicaoFinanceira) {
        this.instituicaoFinanceira = instituicaoFinanceira;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public Boolean getExclusaoLogica() {
        return exclusaoLogica;
    }

    public void setExclusaoLogica(Boolean exclusaoLogica) {
        this.exclusaoLogica = exclusaoLogica;
    }

    public List<Movimentacao> getMovimentacoes() {
        return movimentacoes;
    }

    public void setMovimentacoes(List<Movimentacao> movimentacoes) {
        this.movimentacoes = movimentacoes;
    }
}
