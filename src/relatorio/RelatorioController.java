package com.xpto.desafio.controller;

import com.xpto.desafio.entity.ReceitaXPTO;
import com.xpto.desafio.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @PostMapping("/receita-xpto/{clienteId}")
    public ResponseEntity<ReceitaXPTO> calcularReceitaXPTO(
            @PathVariable Long clienteId,
            @RequestParam String inicio,
            @RequestParam String fim) {
        try {
            LocalDate inicioDate = LocalDate.parse(inicio, formatter);
            LocalDate fimDate = LocalDate.parse(fim, formatter);
            ReceitaXPTO receita = relatorioService.calcularReceitaXPTO(clienteId, inicioDate, fimDate);
            return new ResponseEntity<>(receita, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/saldo-cliente/{clienteId}")
    public ResponseEntity<String> relatorioSaldoCliente(@PathVariable Long clienteId) {
        try {
            String relatorio = relatorioService.gerarRelatorioSaldoCliente(clienteId);
            return new ResponseEntity<>(relatorio, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/saldo-cliente-periodo/{clienteId}")
    public ResponseEntity<String> relatorioSaldoClientePeriodo(
            @PathVariable Long clienteId,
            @RequestParam String inicio,
            @RequestParam String fim) {
        try {
            LocalDate inicioDate = LocalDate.parse(inicio, formatter);
            LocalDate fimDate = LocalDate.parse(fim, formatter);
            String relatorio = relatorioService.gerarRelatorioSaldoClientePorPeriodo(clienteId, inicioDate, fimDate);
            return new ResponseEntity<>(relatorio, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/saldo-todos-clientes")
    public ResponseEntity<String> relatorioSaldoTodosClientes() {
        String relatorio = relatorioService.gerarRelatorioSaldoTodosClientes();
        return new ResponseEntity<>(relatorio, HttpStatus.OK);
    }

    @GetMapping("/receita-xpto-periodo")
    public ResponseEntity<String> relatorioReceitaXPTO(
            @RequestParam String inicio,
            @RequestParam String fim) {
        try {
            LocalDate inicioDate = LocalDate.parse(inicio, formatter);
            LocalDate fimDate = LocalDate.parse(fim, formatter);
            String relatorio = relatorioService.gerarRelatorioReceitaXPTO(inicioDate, fimDate);
            return new ResponseEntity<>(relatorio, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
