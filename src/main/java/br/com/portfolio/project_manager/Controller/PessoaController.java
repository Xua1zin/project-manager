package br.com.portfolio.project_manager.Controller;

import br.com.portfolio.project_manager.Service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {
    @Autowired
    private PessoaService pessoaService;
}
