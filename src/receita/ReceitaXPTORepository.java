package com.xpto.desafio.repository;

import com.xpto.desafio.entity.ReceitaXPTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReceitaXPTORepository extends JpaRepository<ReceitaXPTO, Long> {
    List<ReceitaXPTO> findByClienteId(Long clienteId);
    List<ReceitaXPTO> findByPeriodoInicioBetween(LocalDate inicio, LocalDate fim);
}
