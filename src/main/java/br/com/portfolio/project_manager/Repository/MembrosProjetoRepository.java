package br.com.portfolio.project_manager.Repository;

import br.com.portfolio.project_manager.Model.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembrosProjetoRepository extends JpaRepository<Projeto, Long> {
}
