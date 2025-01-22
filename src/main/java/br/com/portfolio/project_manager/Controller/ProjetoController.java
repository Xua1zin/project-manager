package br.com.portfolio.project_manager.Controller;

import br.com.portfolio.project_manager.Service.ProjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projeto")
public class ProjetoController {
    @Autowired
    private ProjetoService projetoService;
}
