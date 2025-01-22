package br.com.portfolio.project_manager.Service;

import br.com.portfolio.project_manager.Exception.Pessoa.PessoaNotFoundException;
import br.com.portfolio.project_manager.Model.Pessoa;
import br.com.portfolio.project_manager.Repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public String save(Pessoa pessoa){
        try{
            pessoaRepository.save(pessoa);

            return "Person created successfully";
        }catch(Exception e){
            throw new RuntimeException("Error saving person: " + e.getMessage());
        }
    }

    public String delete(Long id){
        try{
            Pessoa pessoa = pessoaRepository.findById(id)
                    .orElseThrow(PessoaNotFoundException::new);

            if(pessoa.getProjetos() != null || pessoa.getProjetosGerenciados() != null){
                throw new ValidationException("People who are linked to project cannot be deleted");
            }

            pessoaRepository.deleteById(id);

            return "Person successfully deleted";
        } catch (PessoaNotFoundException | ValidationException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException("Error deleting person: " + e.getMessage());
        }
    }

    public String update(Pessoa pessoa, Long id){
        try {
            Pessoa existingPessoa = pessoaRepository.findById(id)
                    .orElseThrow(PessoaNotFoundException::new);

            existingPessoa.setNome(pessoa.getNome());
            existingPessoa.setCpf(pessoa.getCpf());
            existingPessoa.setDataNascimento(pessoa.getDataNascimento());
            existingPessoa.setGerente(pessoa.isGerente());
            existingPessoa.setFuncionario(pessoa.isFuncionario());

            pessoaRepository.save(existingPessoa);
            return "Person successfully updated";
        } catch (PessoaNotFoundException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException("Error updating person: " + e.getMessage());
        }
    }

    public Pessoa findById(Long id){
        try{
            return pessoaRepository.findById(id)
                    .orElseThrow(PessoaNotFoundException::new);
        } catch (PessoaNotFoundException e){
            throw e;
        } catch (Exception e){
            throw new RuntimeException("Error finding person: " + e.getMessage());
        }
    }

    public List<Pessoa> findAll(){
        try{
            return pessoaRepository.findAll();
        } catch (Exception e){
            throw new RuntimeException("Error finding people: " + e.getMessage());
        }
    }
}
