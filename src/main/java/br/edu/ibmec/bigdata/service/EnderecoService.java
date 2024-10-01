package br.edu.ibmec.bigdata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ibmec.bigdata.model.Endereco;
import br.edu.ibmec.bigdata.repository.EnderecoRepository;
import jakarta.validation.Valid;

import java.util.List;

@Service
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;

    @Autowired
    public EnderecoService(EnderecoRepository enderecoRepository){
        this.enderecoRepository = enderecoRepository;
    }
    
    public List<Endereco> buscarTodosEnderecos() {
        return enderecoRepository.findAll();
    }

    public Endereco buscarEnderecoPorId(Integer id) {
        return enderecoRepository.findById(id).orElse(null);
    }

    
    // Método para adicionar endereço
    public Endereco adicionarEndereco(@Valid Endereco endereco){
        if (enderecoJaExiste(endereco)) {
            throw new IllegalArgumentException("Endereco já existe para este cliente.");
        }
        return enderecoRepository.save(endereco);
    }

    private boolean enderecoJaExiste(Endereco novoEndereco) {
        List<Endereco> enderecos = enderecoRepository.findAll();
        return enderecos.stream().anyMatch(e ->
                e.getCliente().getId() == novoEndereco.getCliente().getId() &&
                        e.getCep().equals(novoEndereco.getCep()) &&
                        e.getNumero().equals(novoEndereco.getNumero())
        );
    }

    // Método para atualizar endereço
    public Endereco atualizarEndereco(@Valid Endereco endereco){
        // Verifica se o endereço existe antes de tentar atualizá-lo
        if (!enderecoRepository.existsById((endereco.getId()))){
            throw new IllegalArgumentException("Endereço não encontrado.");
        }
        // Verifica se o novo endereço é duplicado, mas ignora a comparação com ele mesmo
        if (enderecoJaExiste(endereco)) {
            throw new IllegalArgumentException("Endereço já existe para este cliente.");
        }
        return enderecoRepository.save(endereco);
    }
    // Método para remover endereço por ID
    public void removerEndereco(int id){
        if (!enderecoRepository.existsById(id)) {
            throw new IllegalArgumentException("Endereço não encontrado.");
        }
        enderecoRepository.deleteById(id);
    }
}
