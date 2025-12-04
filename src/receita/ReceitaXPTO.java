package com.xpto.desafio.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "RECEITA_XPTO")
public class ReceitaXPTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENTE_ID", nullable = false)
    private Cliente cliente;

    @Column(name = "PERIODO_INICIO", nullable = false)
    private LocalDate periodoInicio;

    @Column(name = "PERIODO_FIM", nullable = false)
    private LocalDate periodoFim;

    @Column(name = "QTD_MOVIMENTACOES", nullable = false)
    private Integer qtdMovimentacoes;

    @Column(name = "VALOR_COBRADO", nullable = false)
    private BigDecimal valorCobrado;

    // Construtores
    public ReceitaXPTO() {
    }

    public ReceitaXPTO(Cliente cliente, LocalDate periodoInicio, LocalDate periodoFim, 
                       Integer qtdMovimentacoes, BigDecimal valorCobrado) {
        this.cliente = cliente;
        this.periodoInicio = periodoInicio;
        this.periodoFim = periodoFim;
        this.qtdMovimentacoes = qtdMovimentacoes;
        this.valorCobrado = valorCobrado;
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

    public LocalDate getPeriodoInicio() {
        return periodoInicio;
    }

    public void setPeriodoInicio(LocalDate periodoInicio) {
        this.periodoInicio = periodoInicio;
    }

    public LocalDate getPeriodoFim() {
        return periodoFim;
    }

    public void setPeriodoFim(LocalDate periodoFim) {
        this.periodoFim = periodoFim;
    }

    public Integer getQtdMovimentacoes() {
        return qtdMovimentacoes;
    }

    public void setQtdMovimentacoes(Integer qtdMovimentacoes) {
        this.qtdMovimentacoes = qtdMovimentacoes;
    }

    public BigDecimal getValorCobrado() {
        return valorCobrado;
    }

    public void setValorCobrado(BigDecimal valorCobrado) {
        this.valorCobrado = valorCobrado;
    }
}
