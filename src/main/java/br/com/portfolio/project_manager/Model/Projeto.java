package br.com.portfolio.project_manager.Model;

import br.com.portfolio.project_manager.Model.Enum.Risco;
import br.com.portfolio.project_manager.Model.Enum.Status;
import jakarta.persistence.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "projeto")
public class Projeto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 200, message = "O nome não pode ter mais que 100 caracteres")
    private String nome;

    @NotNull
    private LocalDate dataInicio;

    @NotNull
    private LocalDate dataPrevisaoFim;

    private Instant dataFim;

    @Size(min = 10, max = 5000, message = "A descrição não pode ter mais que 5000 caracteres")
    private String descricao;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    private Float orcamento;

    @Enumerated(EnumType.STRING)
    private Risco risco;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "idgerente", referencedColumnName = "id")
    private Pessoa gerente;

    @ManyToMany(mappedBy = "projetos")
    private List<Pessoa> membros;

    public Projeto() {
    }

    public Projeto(Long id, String nome, LocalDate dataInicio, LocalDate dataPrevisaoFim, Instant dataFim, String descricao, Status status, Float orcamento, Risco risco, Pessoa gerente, List<Pessoa> membros) {
        this.id = id;
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataPrevisaoFim = dataPrevisaoFim;
        this.dataFim = dataFim;
        this.descricao = descricao;
        this.status = status;
        this.orcamento = orcamento;
        this.risco = risco;
        this.gerente = gerente;
        this.membros = membros;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataPrevisaoFim() {
        return dataPrevisaoFim;
    }

    public Instant getDataFim() {
        return dataFim;
    }

    public String getDescricao() {
        return descricao;
    }

    public Status getStatus() {
        return status;
    }

    public Float getOrcamento() {
        return orcamento;
    }

    public Risco getRisco() {
        return risco;
    }

    public Pessoa getGerente() {
        return gerente;
    }

    public List<Pessoa> getMembros() {
        return membros;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public void setDataPrevisaoFim(LocalDate dataPrevisaoFim) {
        this.dataPrevisaoFim = dataPrevisaoFim;
    }

    public void setDataFim(Instant dataFim) {
        this.dataFim = dataFim;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setOrcamento(Float orcamento) {
        this.orcamento = orcamento;
    }

    public void setRisco(Risco risco) {
        this.risco = risco;
    }

    public void setGerente(Pessoa gerente) {
        this.gerente = gerente;
    }

    public void setMembros(List<Pessoa> membros) {
        this.membros = membros;
    }
}
