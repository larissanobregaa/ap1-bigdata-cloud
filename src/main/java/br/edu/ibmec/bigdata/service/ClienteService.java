package br.edu.ibmec.bigdata.service;

import br.edu.ibmec.bigdata.model.Cliente;
import br.edu.ibmec.bigdata.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.List;

import jakarta.validation.Valid;

@Service
@Validated
public class ClienteService {
    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }
    // Método que retorna todos os clientes
    public List<Cliente> buscarClientes() {
        return clienteRepository.findAll();
    }

    public Cliente buscarClientePorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }

    public Cliente adicionarCliente(@Valid Cliente cliente){
        // Verifica se o cliente é maior de idade
        if (!cliente.isMaiorDeIdade()){
            throw new IllegalArgumentException("O cliente deve ter pelo menos 18 anos");
        }
        // Verifica se o email já está cadastrado
        if (clienteRepository.existsByEmail(cliente.getEmail())){
            throw new IllegalArgumentException("Email já cadastrado!");
        }
        // Verifica se o CPF já está cadastrado
        if (clienteRepository.existsByCpf(cliente.getCpf())){
            throw new IllegalArgumentException("CPF já cadastrado!");
        }
        // Salva o novo cliente
        return clienteRepository.save(cliente);
    }

    public List<Cliente> getAllClientes(){
        return clienteRepository.findAll();
    }

    public Cliente atualizarCliente(@Valid Cliente cliente) {
        // Verifica se o cliente existe antes de atualizar
        if (!clienteRepository.existsById(cliente.getId())) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }

        // Verifica se o email já está cadastrado por outro cliente
        Cliente clienteComMesmoEmail = clienteRepository.findByEmail(cliente.getEmail());
        if (clienteComMesmoEmail != null && clienteComMesmoEmail.getId() != cliente.getId()) {
            throw new IllegalArgumentException("Email já cadastrado por outro cliente!");
        }

        // Verifica se o CPF já está cadastrado por outro cliente
        Cliente clienteComMesmoCpf = clienteRepository.findByCpf(cliente.getCpf());
        if (clienteComMesmoCpf != null && clienteComMesmoCpf.getId() != cliente.getId()) {
            throw new IllegalArgumentException("CPF já cadastrado por outro cliente!");
        }

        return clienteRepository.save(cliente);
    }

    public void removerCliente(int id) {
        // Verifica se o cliente existe antes de tentar removê-lo
        if (!clienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }

        clienteRepository.deleteById(id);
    }

}

