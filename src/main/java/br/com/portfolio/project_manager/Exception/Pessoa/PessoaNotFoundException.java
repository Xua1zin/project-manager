package br.com.portfolio.project_manager.Exception.Pessoa;

public class PessoaNotFoundException extends RuntimeException {
    public PessoaNotFoundException(){
        super("Person not found");
    }
}
