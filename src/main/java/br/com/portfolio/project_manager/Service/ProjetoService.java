package br.com.portfolio.project_manager.Service;

import br.com.portfolio.project_manager.Repository.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjetoService {
    @Autowired
    private ProjetoRepository projetoRepository;
}
