package br.com.portfolio.project_manager.Exception.Projeto;

public class ProjetoNotFoundException extends RuntimeException {
    public ProjetoNotFoundException() {
        super("Project not found");
    }
}
