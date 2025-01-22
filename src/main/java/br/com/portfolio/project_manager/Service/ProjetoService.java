package br.com.portfolio.project_manager.Service;

import br.com.portfolio.project_manager.Exception.Projeto.ProjetoNotFoundException;
import br.com.portfolio.project_manager.Model.Enum.Status;
import br.com.portfolio.project_manager.Model.Projeto;
import br.com.portfolio.project_manager.Repository.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

//O sistema deve permitir o cadastro (inserção, exclusão, alteração e consulta) de projetos. Para cada
//projeto devem ser informados: nome, data de início, gerente responsável, previsão de término, data real de
//término, orçamento total, descrição e status.
@Service
public class ProjetoService {
    @Autowired
    private ProjetoRepository projetoRepository;

    public String save(Projeto projeto){
        try{
            if (projeto.getDataInicio().isAfter(projeto.getDataPrevisaoFim())) {
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

    //Se um projeto foi mudado o status para iniciado, em andamento ou encerrado não
    //pode mais ser excluído
    public String delete(Long id){
        try{
            Projeto projeto = projetoRepository.findById(id)
                    .orElseThrow(ProjetoNotFoundException :: new);
            Status status = projeto.getStatus();

            if(status.equals(Status.INICIADO) || status.equals(Status.EM_ANDAMENTO) || status.equals(Status.ENCERRADO)){
                throw new ValidationException("Projects that already have started or finished cannot be deleted.");
            }

            projetoRepository.deleteById(projeto.getId());
            return "Project deleted successfully";
        } catch (ValidationException | ProjetoNotFoundException e){
            throw e;
        } catch (Exception e){
            throw new RuntimeException("Error deleting project: " + e.getMessage());
        }
    }

    //Consultas
    public List<Projeto> findAll(){
        try{
            return projetoRepository.findAll();
        } catch (Exception e){
            throw new RuntimeException("Error finding projects: " + e.getMessage());
        }
    }

    public Projeto findById(Long id){
        try{
            return projetoRepository.findById(id)
                    .orElseThrow(ProjetoNotFoundException :: new);
        } catch (ProjetoNotFoundException e){
            throw e;
        } catch (Exception e){
            throw new RuntimeException("Error finding project: " + e.getMessage());
        }
    }
}
