package br.edu.ibmec.bigdata.controller;

import java.util.List;

import br.edu.ibmec.bigdata.model.Cliente;
import br.edu.ibmec.bigdata.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.ibmec.bigdata.model.Endereco;
import br.edu.ibmec.bigdata.service.EnderecoService;
import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("enderecos")
public class EnderecoController {
    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<Endereco>> getAllEnderecos() {
        List<Endereco> enderecos = enderecoService.buscarTodosEnderecos();
        if (enderecos.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retorna 204 se não houver endereços
        }
        return ResponseEntity.ok(enderecos); // Retorna 200 com a lista de endereços
    }

    @GetMapping("/{id}")
    public ResponseEntity<Endereco> getEnderecoById(@PathVariable Integer id) {
        Endereco endereco = enderecoService.buscarEnderecoPorId(id);
        if (endereco == null) {
            return ResponseEntity.notFound().build(); // Retorna 404 se o endereço não for encontrado
        }
        return ResponseEntity.ok(endereco); // Retorna 200 com o endereço encontrado
    }

    @PostMapping("/clientes/{clienteId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Endereco adicionarEndereco(@PathVariable int clienteId, @Valid @RequestBody Endereco endereco) {
        Cliente cliente = clienteService.buscarCliente(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        endereco.setCliente(cliente);

        return enderecoService.adicionarEndereco(endereco, cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Endereco> atualizarEndereco(@PathVariable int id, @Valid @RequestBody Endereco endereco){
        endereco.setId(id);
        Endereco enderecoAtualizado = enderecoService.atualizarEndereco(endereco);
        return ResponseEntity.ok(enderecoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerEndereco(@PathVariable int id){
        enderecoService.removerEndereco(id);
        return ResponseEntity.noContent().build();
    }
}
