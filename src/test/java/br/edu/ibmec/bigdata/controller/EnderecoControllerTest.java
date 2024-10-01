package br.edu.ibmec.bigdata.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import br.edu.ibmec.bigdata.model.Endereco;
import br.edu.ibmec.bigdata.service.EnderecoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EnderecoController.class)
public class EnderecoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private EnderecoService enderecoService;

    private Endereco endereco;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        endereco = new Endereco();
        endereco.setRua("Rua Teste");
        endereco.setNumero("123");
        endereco.setBairro("Bairro Teste");
        endereco.setCidade("Cidade Teste");
        endereco.setEstado("SP");
        endereco.setCep("12345-678");
    }

    @Test
    public void adicionarEndereco_quandoEnderecoValido_entaoRetornarCreated() throws Exception {
        when(enderecoService.adicionarEndereco(endereco)).thenReturn(endereco);

        mockMvc.perform(post("/enderecos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(endereco)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rua").value("Rua Teste"));
    }

    @Test
    public void adicionarEndereco_quandoRuaEmBranco_entaoRetornarBadRequest() throws Exception {
        endereco.setRua(""); // Simula rua em branco
        when(enderecoService.adicionarEndereco(endereco)).thenThrow(new IllegalArgumentException("Campo rua é obrigatório!"));

        mockMvc.perform(post("/enderecos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(endereco)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Há erros na sua requisição, verique"));
    }

    @Test
    public void getAllEnderecos_quandoNaoExistemEnderecos_entaoRetornarNoContent() throws Exception {
        when(enderecoService.buscarTodosEnderecos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/enderecos"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getAllEnderecos_quandoExistemEnderecos_entaoRetornarOk() throws Exception {
        when(enderecoService.buscarTodosEnderecos()).thenReturn(List.of(endereco));

        mockMvc.perform(get("/enderecos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rua").value("Rua Teste"));
    }

    @Test
    public void buscarEnderecoPorId_quandoIdExistente_entaoRetornarEndereco() throws Exception {
        when(enderecoService.buscarEnderecoPorId(1)).thenReturn(endereco);

        mockMvc.perform(get("/enderecos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rua").value("Rua Teste"));
    }

    @Test
    public void buscarEnderecoPorId_quandoIdNaoExistente_entaoRetornarNotFound() throws Exception {
        when(enderecoService.buscarEnderecoPorId(2)).thenReturn(null);

        mockMvc.perform(get("/enderecos/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void removerEndereco_quandoIdNaoExistente_entaoLancarExcecao() throws Exception {
        doThrow(new IllegalArgumentException("Endereço não encontrado.")).when(enderecoService).removerEndereco(1);

    mockMvc.perform(delete("/enderecos/1")) 
            .andExpect(status().isNotFound()) 
            .andExpect(jsonPath("$.message").value("Endereço não encontrado.")); 
    }
}