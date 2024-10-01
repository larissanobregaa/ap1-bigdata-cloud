package br.edu.ibmec.bigdata.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ibmec.bigdata.model.Endereco;
import br.edu.ibmec.bigdata.service.EnderecoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {
    @Autowired
    private EnderecoService enderecoService;
    
    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

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

    @PostMapping
    public ResponseEntity<Endereco> adicionarEndereco(@Valid @RequestBody Endereco endereco) {
        Endereco novoEndereco = enderecoService.adicionarEndereco(endereco);
        return ResponseEntity.ok(novoEndereco);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Endereco> atualizarEndereco(@PathVariable int id, @org.jetbrains.annotations.NotNull @Valid @RequestBody Endereco endereco){
        endereco.setId(id);
        Endereco enderecoAtualizado = enderecoService.atualizarEndereco(endereco);
        return ResponseEntity.ok(enderecoAtualizado);
    }

    @DeleteMapping("/id")
    public ResponseEntity<Void> removerEndereco(@PathVariable int id){
        enderecoService.removerEndereco(id);
        return ResponseEntity.noContent().build();
    }
}
