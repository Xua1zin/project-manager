package br.com.portfolio.project_manager.Service;

import br.com.portfolio.project_manager.Model.Enum.Status;
import br.com.portfolio.project_manager.Model.Projeto;
import br.com.portfolio.project_manager.ProjectManagerApplication;
import br.com.portfolio.project_manager.Repository.ProjetoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest(classes = ProjectManagerApplication.class)
class ProjetoServiceTest {

    @InjectMocks
    ProjetoService projetoService;

    @Mock
    ProjetoRepository projetoRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveSuccess() {
        Projeto projeto = new Projeto();
        projeto.setNome("projeto");
        projeto.setDataInicio(Date.from(LocalDate.of(2025, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        projeto.setDataPrevisaoFim(Date.from(LocalDate.of(2025, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        projeto.setOrcamento(5000f);
        projeto.setStatus(Status.EM_ANALISE);

        when(projetoRepository.save(projeto)).thenReturn(projeto);
        String result = projetoService.save(projeto);

        assertEquals("Project created successfully", result);
        verify(projetoRepository, times(1)).save(projeto);
    }

    @Test
    void saveInvalidDataInicio() {
        Projeto projeto = new Projeto();
        projeto.setNome("projeto");
        projeto.setDataInicio(Date.from(LocalDate.of(2025, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        projeto.setDataPrevisaoFim(Date.from(LocalDate.of(2025, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        projeto.setOrcamento(5000f);
        projeto.setStatus(Status.EM_ANALISE);

        assertThrows(ValidationException.class, () -> projetoService.save(projeto));
    }

    @Test
    void saveInvalidOrcamento() {
        Projeto projeto = new Projeto();
        projeto.setNome("projeto");
        projeto.setDataInicio(Date.from(LocalDate.of(2025, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        projeto.setDataPrevisaoFim(Date.from(LocalDate.of(2025, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        projeto.setOrcamento(-100f);
        projeto.setStatus(Status.EM_ANALISE);

        assertThrows(ValidationException.class, () -> projetoService.save(projeto));
    }

    @Test
    void saveException(){
        Projeto projeto = new Projeto();
        when(projetoRepository.save(projeto)).thenThrow(new RuntimeException("Error registering project: "));
        assertThrows(RuntimeException.class, () -> projetoService.save(projeto));
    }
}