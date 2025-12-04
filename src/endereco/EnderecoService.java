package com.xpto.desafio.service;

import com.xpto.desafio.entity.Endereco;
import com.xpto.desafio.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    /**
     * Obtém um endereço pelo ID.
     */
    public Optional<Endereco> obterEnderecoPorId(Long id) {
        return enderecoRepository.findById(id);
    }

    /**
     * Obtém todos os endereços de um cliente.
     */
    public List<Endereco> obterEnderecosPorCliente(Long clienteId) {
        return enderecoRepository.findByClienteId(clienteId);
    }

    /**
     * Cria um novo endereço.
     */
    @Transactional
    public Endereco criarEndereco(Endereco endereco) {
        return enderecoRepository.save(endereco);
    }

    /**
     * Atualiza um endereço.
     */
    @Transactional
    public Endereco atualizarEndereco(Long id, Endereco enderecoAtualizado) {
        Optional<Endereco> enderecoOpt = enderecoRepository.findById(id);
        if (enderecoOpt.isPresent()) {
            Endereco endereco = enderecoOpt.get();
            endereco.setLogradouro(enderecoAtualizado.getLogradouro());
            endereco.setNumero(enderecoAtualizado.getNumero());
            endereco.setComplemento(enderecoAtualizado.getComplemento());
            endereco.setBairro(enderecoAtualizado.getBairro());
            endereco.setCidade(enderecoAtualizado.getCidade());
            endereco.setUf(enderecoAtualizado.getUf());
            endereco.setCep(enderecoAtualizado.getCep());
            return enderecoRepository.save(endereco);
        }
        throw new IllegalArgumentException("Endereço não encontrado com ID: " + id);
    }

    /**
     * Deleta um endereço.
     */
    @Transactional
    public void deletarEndereco(Long id) {
        enderecoRepository.deleteById(id);
    }
}
