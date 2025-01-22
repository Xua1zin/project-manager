package br.com.portfolio.project_manager.Service;

import br.com.portfolio.project_manager.Exception.Pessoa.PessoaNotFoundException;
import br.com.portfolio.project_manager.Exception.Projeto.ProjetoNotFoundException;
import br.com.portfolio.project_manager.Model.Enum.Risco;
import br.com.portfolio.project_manager.Model.Enum.Status;
import br.com.portfolio.project_manager.Model.Pessoa;
import br.com.portfolio.project_manager.Model.Projeto;
import br.com.portfolio.project_manager.Repository.PessoaRepository;
import br.com.portfolio.project_manager.Repository.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

//O sistema deve permitir o cadastro (inserção, exclusão, alteração e consulta) de projetos. Para cada
//projeto devem ser informados: nome, data de início, gerente responsável, previsão de término, data real de
//término, orçamento total, descrição e status.
@Service
public class ProjetoService {
    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    public String save(Projeto projeto){
        try{
            //A previsão de fim não pode ser antes da data de inicio
            if (projeto.getDataInicio().isAfter(projeto.getDataPrevisaoFim())) {
                throw new ValidationException("A data de início não pode ser após a data de previsão de término.");
            }

            //O orçamento não pode ser negativo
            if (projeto.getOrcamento() < 0) {
                throw new ValidationException("O orçamento não pode ser negativo.");
            }

            //Toda vez que um projeto for criado, ele começa do status inicial, no caso "Em análise"
            projeto.setStatus(Status.EM_ANALISE);
            projeto.setRisco(calculateRisco(projeto));
            projetoRepository.save(projeto);

            return "Project created successfully";
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e){
            throw new RuntimeException("Error registering project: " + e.getMessage());
        }
    }

    public String delete(Long id){
        try{
            //Se não encontrar o projeto, dispara o erro de não encontrado
            Projeto projeto = projetoRepository.findById(id)
                    .orElseThrow(ProjetoNotFoundException :: new);

            //Entendimento mais fácil e economiza linha
            Status status = projeto.getStatus();

            //Se um projeto foi mudado o status para iniciado, em andamento ou encerrado não pode mais ser excluído
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

    public String update(Projeto projeto, Long id){
        try {
            Projeto existingProjeto = projetoRepository.findById(id)
                    .orElseThrow(ProjetoNotFoundException :: new);

            //Não diz nas regras de negócio passadas pelo escopo, mas deixarei que não pode mudar data de inicio nem a de fim,
            //nem adicionar Membros, já que tem uma função somente para isso.
            if(projeto.getNome() != null) {
                existingProjeto.setNome(projeto.getNome());
            }
            if(projeto.getDataPrevisaoFim() != null) {
                existingProjeto.setDataPrevisaoFim(projeto.getDataPrevisaoFim());
            }
            if(projeto.getOrcamento() != null) {
                existingProjeto.setOrcamento(projeto.getOrcamento());
                existingProjeto.setRisco(calculateRisco(existingProjeto));
            }
            if(projeto.getStatus() != null) {
                //fiz uma função de mapeamento em que o Status pode mudar, de acordo com isso, se ele mudar de status para um que
                //não pode, ele retorna um ValidationException
                if (!isStatusTransitionValid(existingProjeto.getStatus(), projeto.getStatus())) {
                    throw new ValidationException("Invalid status transition: "
                            + existingProjeto.getStatus() + " -> " + projeto.getStatus());
                }
                existingProjeto.setStatus(projeto.getStatus());
            }
            if(projeto.getDescricao() != null) {
                existingProjeto.setDescricao(projeto.getDescricao());
            }
            if(projeto.getGerente() != null) {
                existingProjeto.setGerente(projeto.getGerente());
            }

            projetoRepository.save(existingProjeto);

            return "Project updated successfully";
        } catch (ProjetoNotFoundException | ValidationException e){
            throw e;
        } catch (Exception e){
            throw new RuntimeException("Error updating project: " + e.getMessage());
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

    public String addMembrosToProjeto(List<Long> idPessoa, Long idProjeto){
        try{
            List<Pessoa> pessoas = pessoaRepository.findAllById(idPessoa);

            //retorna erro de pessoa não encontrada por Ids não encontrados
            if (pessoas.size() != idPessoa.size()) {
                throw new PessoaNotFoundException();
            }

            //Filtro que verifica se a pessoa é funcionária
            List<Pessoa> pessoaList = pessoas.stream()
                    .filter(Pessoa::isFuncionario)
                    .collect(Collectors.toList());

            if (pessoaList.isEmpty()) {
                throw new RuntimeException("No valid employees found in the provided list.");
            }

            Projeto projeto = projetoRepository.findById(idProjeto)
                    .orElseThrow(ProjetoNotFoundException :: new);

            projeto.setMembros(pessoaList);

            projetoRepository.save(projeto);

            return "Membros added to Projeto successfully";
        } catch (PessoaNotFoundException | ProjetoNotFoundException e){
            throw e;
        } catch (Exception e){
            throw new RuntimeException("Error vinculating Membros to Projeto: " + e.getMessage());
        }
    }

    private Risco calculateRisco(Projeto projeto) {
        if (projeto.getOrcamento() > 50000) {
            return Risco.ALTO;
        } else if (projeto.getOrcamento() > 20000) {
            return Risco.MEDIO;
        } else {
            return Risco.BAIXO;
        }
    }

    private boolean isStatusTransitionValid(Status currentStatus, Status newStatus) {
        return switch (currentStatus) {
            case EM_ANALISE -> newStatus == Status.ANALISE_REALIZADA;
            case ANALISE_REALIZADA -> newStatus == Status.ANALISE_APROVADA || newStatus == Status.CANCELADO;
            case ANALISE_APROVADA -> newStatus == Status.INICIADO;
            case INICIADO -> newStatus == Status.PLANEJADO;
            case PLANEJADO -> newStatus == Status.EM_ANDAMENTO;
            case EM_ANDAMENTO -> newStatus == Status.ENCERRADO || newStatus == Status.CANCELADO;
            // Não pode mudar de status depois de encerrado/cancelado
            case ENCERRADO, CANCELADO -> false;
        };
    }
}
