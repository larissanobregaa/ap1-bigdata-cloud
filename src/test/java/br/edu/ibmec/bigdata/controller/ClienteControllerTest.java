package br.edu.ibmec.bigdata.controller;

import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import br.edu.ibmec.bigdata.model.Cliente;
import br.edu.ibmec.bigdata.service.ClienteService;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ClienteService clienteService;
    private Cliente cliente;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cliente = new Cliente();
        cliente.setNome("Teste Cliente");
        cliente.setEmail("teste@cliente.com");
        cliente.setCpf("123.456.789-00");
    }

    @Test
    public void getAllClientes_quandoNaoExistemClientes_entaoRetornarNoContent() throws Exception {
        when(clienteService.buscarClientes()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getAllClientes_quandoExistemClientes_entaoRetornarOk() throws Exception {
        when(clienteService.buscarClientes()).thenReturn(List.of(cliente));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Teste Cliente"));
    }

    @Test
    public void adicionarCliente_quandoClienteValido_entaoRetornarCreated() throws Exception {
        when(clienteService.adicionarCliente(cliente)).thenReturn(cliente);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(cliente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Teste Cliente"));
    }

    @Test
    public void adicionarCliente_quandoEmailDuplicado_entaoRetornarBadRequest() throws Exception {
        when(clienteService.adicionarCliente(cliente)).thenThrow(new IllegalArgumentException("Email já cadastrado!"));

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(cliente)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Há erros na sua requisição, verique"));
    }
}
