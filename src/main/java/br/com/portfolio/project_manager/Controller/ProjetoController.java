package br.com.portfolio.project_manager.Controller;

import br.com.portfolio.project_manager.Service.ProjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/projeto")
public class ProjetoController {
    @Autowired
    private ProjetoService projetoService;
}
