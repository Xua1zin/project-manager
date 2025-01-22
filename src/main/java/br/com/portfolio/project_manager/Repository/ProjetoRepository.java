package br.com.portfolio.project_manager.Repository;

import br.com.portfolio.project_manager.Model.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
}
