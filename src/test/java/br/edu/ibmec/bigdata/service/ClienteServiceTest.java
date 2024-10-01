package br.edu.ibmec.bigdata.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.edu.ibmec.bigdata.model.Cliente;
import br.edu.ibmec.bigdata.repository.ClienteRepository;

public class ClienteServiceTest {
    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;
    private Cliente cliente;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cliente = new Cliente();
        cliente.setNome("Teste Cliente");
        cliente.setEmail("teste@cliente.com");
        cliente.setCpf("123.456.789-00");
        cliente.setDataNascimento(LocalDate.of(2000, 1, 1));
    }

    @Test
    public void adicionarCliente_quandoNomeMuitoCurto_entaoLancarExcecao() {
        cliente.setNome("Jo"); // Nome muito curto

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.adicionarCliente(cliente);
        });

        assertEquals("O nome deve ter entre 3 e 100 caracteres", exception.getMessage());
    }

    @Test
    public void adicionarCliente_quandoClienteValido_entaoSalvar() {
        when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(false);
        when(clienteRepository.existsByCpf(cliente.getCpf())).thenReturn(false);
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente result = clienteService.adicionarCliente(cliente);

        assertNotNull(result);
        verify(clienteRepository, times(1)).save(cliente);
    }


    @Test
    public void adicionarCliente_quandoEmailInvalido_entaoLancarExcecao() {
        cliente.setEmail("emailInvalido"); // Email sem formato válido

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.adicionarCliente(cliente);
        });

        assertEquals("Campo email deve ser válido!", exception.getMessage());
    }

    @Test
    public void adicionarCliente_quandoEmailDuplicado_entaoLancarExcecao() {
        when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.adicionarCliente(cliente);
        });

        assertEquals("Email já cadastrado!", exception.getMessage());
    }


    @Test
    public void adicionarCliente_quandoCpfFormatoInvalido_entaoLancarExcecao() {
        cliente.setCpf("12345678900"); // CPF fora do formato

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.adicionarCliente(cliente);
        });

        assertEquals("CPF deve seguir o padrão XXX.XXX.XXX-XX", exception.getMessage());
    }


    @Test
    public void adicionarCliente_quandoCpfDuplicado_entaoLancarExcecao() {
        when(clienteRepository.existsByCpf(cliente.getCpf())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.adicionarCliente(cliente);
        });

        assertEquals("CPF já cadastrado!", exception.getMessage());
    }


    @Test
    public void adicionarCliente_quandoDataNascimentoNula_entaoLancarExcecao() {
        cliente.setDataNascimento(null); // Simula cliente sem data de nascimento

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.adicionarCliente(cliente);
        });

        assertEquals("Data de nascimento não pode ser nula", exception.getMessage());
    }

    @Test
    public void adicionarCliente_quandoTelefoneInvalido_entaoLancarExcecao() {
        cliente.setTelefone("12345"); // Telefone fora do formato

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.adicionarCliente(cliente);
        });

        assertEquals("Telefone deve seguir o padrão (XX) XXXXX-XXXX", exception.getMessage());
    }

    @Test
    public void adicionarCliente_quandoClienteMenorDeIdade_entaoLancarExcecao() {
        cliente.setDataNascimento(LocalDate.now().minusYears(17)); // Menor de idade

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.adicionarCliente(cliente);
        });

        assertEquals("Cliente deve ser maior de idade!", exception.getMessage());

    }
}