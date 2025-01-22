package br.com.portfolio.project_manager.Model;


import jakarta.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pessoa")
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 100, message = "O nome não pode ter menos que 2 caracteres e mais que 100")
    private String nome;

    private LocalDate dataNascimento;

    private String cpf;

    private boolean funcionario = false;

    private boolean gerente = false;

    @OneToMany(mappedBy = "gerente")
    private List<Projeto> projetosGerenciados;

    @ManyToMany
    @JoinTable(
            name ="membros_projeto",
            joinColumns = @JoinColumn(name = "idpessoa"),
            inverseJoinColumns = @JoinColumn(name = "idprojeto")
    )
    private List<Projeto> projetos;

    public Pessoa(){
    }

    public Pessoa(Long id, String nome, LocalDate dataNascimento, String cpf, boolean funcionario, boolean gerente, List<Projeto> projetosGerenciados, List<Projeto> projetos) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.funcionario = funcionario;
        this.gerente = gerente;
        this.projetosGerenciados = projetosGerenciados;
        this.projetos = projetos;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public boolean isFuncionario() {
        return funcionario;
    }

    public boolean isGerente() {
        return gerente;
    }

    public List<Projeto> getProjetosGerenciados() {
        return projetosGerenciados;
    }

    public List<Projeto> getProjetos() {
        return projetos;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setFuncionario(boolean funcionario) {
        this.funcionario = funcionario;
    }

    public void setGerente(boolean gerente) {
        this.gerente = gerente;
    }

    public void setProjetosGerenciados(List<Projeto> projetosGerenciados) {
        this.projetosGerenciados = projetosGerenciados;
    }

    public void setProjetos(List<Projeto> projetos) {
        this.projetos = projetos;
    }
}
