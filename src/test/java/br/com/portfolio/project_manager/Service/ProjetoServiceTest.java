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
import java.util.Optional;

import static java.time.LocalDate.of;
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

    //=======================TESTES DE SAVE=======================
    @Test
    void saveSuccess() {
        Projeto projeto = new Projeto();
        projeto.setNome("projeto");
        projeto.setDataInicio(LocalDate.of(2025, 1, 1));
        projeto.setDataPrevisaoFim(LocalDate.from(of(2025, 12, 31)));
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
        projeto.setDataInicio(LocalDate.from(of(2025, 12, 31)));
        projeto.setDataPrevisaoFim(LocalDate.from(of(2025, 1, 1)));
        projeto.setOrcamento(5000f);
        projeto.setStatus(Status.EM_ANALISE);

        assertThrows(ValidationException.class, () -> projetoService.save(projeto));
        verify(projetoRepository, never()).save(projeto);
    }

    @Test
    void saveInvalidOrcamento() {
        Projeto projeto = new Projeto();
        projeto.setNome("projeto");
        projeto.setDataInicio(LocalDate.from(of(2025, 1, 1)));
        projeto.setDataPrevisaoFim(LocalDate.from(of(2025, 12, 31)));
        projeto.setOrcamento(-100f);
        projeto.setStatus(Status.EM_ANALISE);

        assertThrows(ValidationException.class, () -> projetoService.save(projeto));
        verify(projetoRepository, never()).save(projeto);
    }

    @Test
    void saveException(){
        Projeto projeto = new Projeto();
        when(projetoRepository.save(projeto)).thenThrow(new RuntimeException("Error registering project: "));

        assertThrows(RuntimeException.class, () -> projetoService.save(projeto));
        verify(projetoRepository, never()).save(projeto);
    }

    //=======================TESTES DE DELETE=======================
    @Test
    void deleteSuccess(){
        Projeto projeto = new Projeto();
        projeto.setId(1L);
        projeto.setNome("projeto");
        projeto.setDataInicio(LocalDate.from(of(2025, 1, 1)));
        projeto.setDataPrevisaoFim(LocalDate.from(of(2025, 12, 31)));
        projeto.setOrcamento(4000f);
        projeto.setStatus(Status.EM_ANALISE);

        when(projetoRepository.findById(projeto.getId())).thenReturn(Optional.of(projeto));
        doNothing().when(projetoRepository).deleteById(projeto.getId());

        String result = projetoService.delete(projeto.getId());
        assertEquals("Project deleted successfully", result);

        verify(projetoRepository, times(1)).deleteById(projeto.getId());
    }

    @Test
    void deleteInvalidStats(){
        Projeto projeto = new Projeto();
        projeto.setId(1L);
        projeto.setNome("projeto");
        projeto.setDataInicio(LocalDate.from(of(2025, 1, 1)));
        projeto.setDataPrevisaoFim(LocalDate.from(of(2025, 12, 31)));
        projeto.setOrcamento(4000f);
        projeto.setStatus(Status.INICIADO);

        when(projetoRepository.findById(projeto.getId())).thenReturn(Optional.of(projeto));
        assertThrows(ValidationException.class, () -> projetoService.delete(projeto.getId()));

        verify(projetoRepository, never()).deleteById(projeto.getId());
    }

    @Test
    void deleteProjectNotFound(){
        Long id = 2L;
        when(projetoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> projetoService.delete(id));

        verify(projetoRepository, never()).deleteById(id);
    }
}