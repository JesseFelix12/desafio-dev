package com.xpto.desafio.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "MOVIMENTACAO")
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTA_ID", nullable = false)
    private Conta conta;

    @Column(name = "DATA_MOVIMENTACAO", nullable = false)
    private LocalDateTime dataMovimentacao;

    @Column(name = "TIPO", nullable = false, length = 10)
    private String tipo; // "CREDITO" ou "DEBITO"

    @Column(name = "VALOR", nullable = false)
    private BigDecimal valor;

    @Column(name = "DESCRICAO")
    private String descricao;

    // Construtores
    public Movimentacao() {
    }

    public Movimentacao(Conta conta, String tipo, BigDecimal valor, String descricao) {
        this.conta = conta;
        this.dataMovimentacao = LocalDateTime.now();
        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public LocalDateTime getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(LocalDateTime dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
