package br.edu.ibmec.bigdata.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.edu.ibmec.bigdata.model.Endereco;
import br.edu.ibmec.bigdata.repository.EnderecoRepository;

public class EnderecoServiceTest {

    @Mock
    private EnderecoRepository enderecoRepository;

    @InjectMocks
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
    public void adicionarEndereco_quandoEnderecoValido_entaoSalvar() {
        when(enderecoRepository.save(endereco)).thenReturn(endereco);

        Endereco result = enderecoService.adicionarEndereco(endereco);

        assertNotNull(result);
        assertEquals("Rua Teste", result.getRua());
        verify(enderecoRepository, times(1)).save(endereco);
    }

    @Test
    public void adicionarEndereco_quandoRuaEmBranco_entaoLancarExcecao() {
        endereco.setRua(""); // Simula rua em branco

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            enderecoService.adicionarEndereco(endereco);
        });

        assertEquals("Campo rua é obrigatório!", exception.getMessage());
    }

    @Test
    public void adicionarEndereco_quandoNumeroEmBranco_entaoLancarExcecao() {
        endereco.setNumero(""); // Simula número em branco

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            enderecoService.adicionarEndereco(endereco);
        });

        assertEquals("Campo número é obrigatório!", exception.getMessage());
    }

    @Test
    public void adicionarEndereco_quandoBairroEmBranco_entaoLancarExcecao() {
        endereco.setBairro(""); // Simula bairro em branco

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            enderecoService.adicionarEndereco(endereco);
        });

        assertEquals("Campo bairro é obrigatório!", exception.getMessage());
    }

    @Test
    public void adicionarEndereco_quandoCidadeEmBranco_entaoLancarExcecao() {
        endereco.setCidade(""); // Simula cidade em branco

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            enderecoService.adicionarEndereco(endereco);
        });

        assertEquals("Campo cidade é obrigatório!", exception.getMessage());
    }

    @Test
    public void adicionarEndereco_quandoEstadoInvalido_entaoLancarExcecao() {
        endereco.setEstado("XX"); // Simula estado inválido

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            enderecoService.adicionarEndereco(endereco);
        });

        assertEquals("Estado deve ser um valor válido (ex.: SP, RJ)", exception.getMessage());
    }

    @Test
    public void adicionarEndereco_quandoCepInvalido_entaoLancarExcecao() {
        endereco.setCep("12345678"); // Simula CEP inválido

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            enderecoService.adicionarEndereco(endereco);
        });

        assertEquals("CEP deve seguir o formato XXXXX-XXX", exception.getMessage());
    }

}
