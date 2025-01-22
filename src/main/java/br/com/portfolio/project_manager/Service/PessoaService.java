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
}
