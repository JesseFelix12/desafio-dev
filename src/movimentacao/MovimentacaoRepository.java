package com.xpto.desafio.repository;

import com.xpto.desafio.entity.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
    List<Movimentacao> findByContaId(Long contaId);
    List<Movimentacao> findByContaIdAndDataMovimentacaoBetween(Long contaId, LocalDateTime inicio, LocalDateTime fim);
}
