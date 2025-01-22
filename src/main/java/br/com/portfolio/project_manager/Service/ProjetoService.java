package br.com.portfolio.project_manager.Service;

import br.com.portfolio.project_manager.Model.Enum.Status;
import br.com.portfolio.project_manager.Model.Projeto;
import br.com.portfolio.project_manager.Repository.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;

//[ ] O sistema deve permitir o cadastro (inserção, exclusão, alteração e consulta) de projetos. Para cada
//projeto devem ser informados: nome, data de início, gerente responsável, previsão de término, data real de
//término, orçamento total, descrição e status.
@Service
public class ProjetoService {
    @Autowired
    private ProjetoRepository projetoRepository;

    public String save(Projeto projeto){
        try{
            if (projeto.getDataInicio().after(projeto.getDataPrevisaoFim())) {
                throw new ValidationException("A data de início não pode ser após a data de previsão de término.");
            }

            if (projeto.getOrcamento() < 0) {
                throw new ValidationException("O orçamento não pode ser negativo.");
            }

            projeto.setStatus(Status.EM_ANALISE);
            projetoRepository.save(projeto);

            return "Project created successfully";
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e){
            throw new RuntimeException("Error registering project: " + e.getMessage());
        }
    }
}
