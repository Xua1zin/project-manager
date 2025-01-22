package br.com.portfolio.project_manager.Controller;

import br.com.portfolio.project_manager.Exception.Pessoa.PessoaNotFoundException;
import br.com.portfolio.project_manager.Model.Pessoa;
import br.com.portfolio.project_manager.Service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import java.util.List;

@Controller
@RequestMapping("/pessoa")
public class PessoaController {
    @Autowired
    private PessoaService pessoaService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Pessoa pessoa){
        try {
            return ResponseEntity.ok(pessoaService.save(pessoa));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        try {
            return ResponseEntity.ok(pessoaService.delete(id));
        } catch (PessoaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Pessoa não encontrada");
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody Pessoa pessoa){
        try{
            return ResponseEntity.ok(pessoaService.update(pessoa, id));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Pessoa> findById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(pessoaService.findById(id));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Pessoa>> findAll(){
        try {
            return ResponseEntity.ok(pessoaService.findAll());
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
