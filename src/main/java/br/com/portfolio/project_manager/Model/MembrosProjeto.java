package br.com.portfolio.project_manager.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "membros_projeto")
public class MembrosProjeto {
    @Id
    @ManyToOne
    @JoinColumn(name = "idpessoa")
    private Pessoa pessoa;

    @Id
    @ManyToOne
    @JoinColumn(name = "idprojeto")
    private Projeto projeto;

    public MembrosProjeto() {
    }

    public MembrosProjeto(Pessoa pessoa, Projeto projeto) {
        this.pessoa = pessoa;
        this.projeto = projeto;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }
}
