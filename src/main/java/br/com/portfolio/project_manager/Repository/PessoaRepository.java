package br.com.portfolio.project_manager.Repository;

import br.com.portfolio.project_manager.Model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}
