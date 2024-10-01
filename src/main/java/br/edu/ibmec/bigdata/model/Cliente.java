package br.edu.ibmec.bigdata.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import  jakarta.validation.constraints.Email;
import jakarta.persistence.CascadeType;
import  jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @NotBlank(message = "Campo nome é obrigatório!")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @Column(unique = true)
    @NotBlank(message = "Campo email é obrigatório!")
    @Email(message = "Campo email deve ser válido!")
    private String email;

    @Column(unique = true)
    @NotBlank(message = "Campo CPF é obrigatório!")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve seguir o padrão XXX.XXX.XXX-XX")
    private String cpf;

    @Column
    @NotNull(message = "Campo data de nascimento é obrigatório!")
    private LocalDate dataNascimento;

    @Column
    @Pattern(regexp = "\\(\\d{2}\\) \\d{5}-\\d{4}", message = "Telefone deve seguir o padrão (XX) XXXXX-XXXX")
    private String telefone;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Endereco> enderecos;

    public boolean isMaiorDeIdade() {
        return Period.between(this.dataNascimento, LocalDate.now()).getYears() >= 18;
    }
}


