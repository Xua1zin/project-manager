package br.com.portfolio.project_manager.Repository;

import br.com.portfolio.project_manager.Model.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
}
