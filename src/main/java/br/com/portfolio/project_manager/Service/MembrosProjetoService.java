package br.com.portfolio.project_manager.Service;

import br.com.portfolio.project_manager.Repository.MembrosProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MembrosProjetoService {
    @Autowired
    private MembrosProjetoRepository membrosProjetoRepository;
}
