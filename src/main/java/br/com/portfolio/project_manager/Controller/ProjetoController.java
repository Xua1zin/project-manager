package br.com.portfolio.project_manager.Controller;

import br.com.portfolio.project_manager.Model.Projeto;
import br.com.portfolio.project_manager.Service.ProjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/projeto")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    @GetMapping("/list")
    public String findAll(Model model) {
        List<Projeto> projetos = projetoService.findAll();
        model.addAttribute("projetos", projetos);
        return "projeto/list";
    }

    @GetMapping("/new")
    public String createProjetoForm(Model model) {
        model.addAttribute("projeto", new Projeto());
        return "projeto/form";
    }

    @PostMapping("/save")
    public String saveProjeto(@ModelAttribute("projeto") Projeto projeto) {
        projetoService.save(projeto);
        return "redirect:/projeto/list";
    }

    @GetMapping("/{id}")
    public String showProjetoDetails(@PathVariable("id") Long id, Model model) {
        Projeto projeto = projetoService.findById(id);
        model.addAttribute("projeto", projeto);
        return "projeto/details";
    }

    @GetMapping("/edit/{id}")
    public String editProjetoForm(@PathVariable("id") Long id, Model model) {
        Projeto projeto = projetoService.findById(id);
        model.addAttribute("projeto", projeto);
        return "projeto/form";
    }

    @PostMapping("/update/{id}")
    public String updateProjeto(@PathVariable("id") Long id, @ModelAttribute("projeto") Projeto projeto) {
        projetoService.update(projeto, id);
        return "redirect:/projeto/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteProjeto(@PathVariable("id") Long id) {
        projetoService.delete(id);
        return "redirect:/projeto/list";
    }

    @PostMapping("/addMembros/{id}")
    public String addMembrosToProjeto(@PathVariable("id") Long idProjeto, @RequestParam List<Long> idPessoa) {
        projetoService.addMembrosToProjeto(idPessoa, idProjeto);
        return "redirect:/projeto/list";
    }
}
