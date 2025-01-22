package br.com.portfolio.project_manager.Service;

import br.com.portfolio.project_manager.Exception.Pessoa.PessoaNotFoundException;
import br.com.portfolio.project_manager.Exception.Projeto.ProjetoNotFoundException;
import br.com.portfolio.project_manager.Model.Enum.Risco;
import br.com.portfolio.project_manager.Model.Enum.Status;
import br.com.portfolio.project_manager.Model.Pessoa;
import br.com.portfolio.project_manager.Model.Projeto;
import br.com.portfolio.project_manager.ProjectManagerApplication;
import br.com.portfolio.project_manager.Repository.PessoaRepository;
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
import java.util.*;

import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest(classes = ProjectManagerApplication.class)
class ProjetoServiceTest {

    @InjectMocks
    ProjetoService projetoService;

    @Mock
    ProjetoRepository projetoRepository;

    @Mock
    PessoaRepository pessoaRepository;


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

        assertThrows(ProjetoNotFoundException.class, () -> projetoService.delete(id));

        verify(projetoRepository, never()).deleteById(id);
    }

    @Test
    void deleteException(){
        when(projetoRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> projetoService.delete(1L));
        assertEquals("Error deleting project: Database error", exception.getMessage());

        verify(projetoRepository, never()).deleteById(1L);
    }

    //=======================TESTES DE UPDATE=======================
    @Test
    void updateSuccess(){
        Projeto projeto = new Projeto();
        projeto.setId(1L);
        projeto.setNome("projeto");
        projeto.setDataInicio(LocalDate.from(of(2025, 1, 1)));
        projeto.setDataPrevisaoFim(LocalDate.from(of(2025, 12, 31)));
        projeto.setDataFim(null);
        projeto.setDescricao("Descrição do projeto");
        projeto.setOrcamento(1000f);
        projeto.setRisco(Risco.ALTO);
        projeto.setStatus(Status.EM_ANALISE);
        projeto.setGerente(new Pessoa());
        projeto.setMembros(null);

        Projeto projetoAtualizado = new Projeto();
        projetoAtualizado.setNome("Projeto Teste Atualizado");
        projetoAtualizado.setDataInicio(LocalDate.from(of(2025, 1, 1)));
        projetoAtualizado.setDataPrevisaoFim(LocalDate.from(of(2025, 2, 15)));
        projetoAtualizado.setDataFim(null);
        projetoAtualizado.setDescricao("Descrição do projeto atualizada");
        projetoAtualizado.setOrcamento(7000f);
        projetoAtualizado.setRisco(Risco.BAIXO);
        projetoAtualizado.setStatus(Status.ANALISE_REALIZADA);
        projetoAtualizado.setGerente(new Pessoa());
        projetoAtualizado.setMembros(null);

        when(projetoRepository.findById(projeto.getId())).thenReturn(Optional.of(projeto));

        when(projetoRepository.save(any(Projeto.class))).thenReturn(projetoAtualizado);

        String result = projetoService.update(projetoAtualizado, projeto.getId());

        assertEquals("Project updated successfully", result);

        verify(projetoRepository).save(any(Projeto.class));

        assertEquals("Projeto Teste Atualizado", projetoAtualizado.getNome());
        assertEquals(LocalDate.from(of(2025, 2, 15)), projetoAtualizado.getDataPrevisaoFim());
        assertEquals(7000f, projetoAtualizado.getOrcamento());
        assertEquals("Descrição do projeto atualizada", projetoAtualizado.getDescricao());
    }

    @Test
    void UpdateProjetoNotFound() {
        Projeto projetoAtualizado = new Projeto();

        when(projetoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProjetoNotFoundException.class, () -> projetoService.update(projetoAtualizado, 1L));

        verify(projetoRepository, never()).save(any(Projeto.class));
    }

    @Test
    void testUpdateException() {
        Projeto projeto = new Projeto();
        Projeto projetoAtualizado = new Projeto();

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));

        when(projetoRepository.save(any(Projeto.class))).thenThrow(new RuntimeException("Unexpected error"));

        assertThrows(RuntimeException.class, () -> projetoService.update(projetoAtualizado, 1L));
    }

    //=======================TESTES DE CONSULTA=======================
    @Test
    void findAllSuccess(){
        List<Projeto> projetoList = new ArrayList<>();

        Projeto projeto = new Projeto();
        projeto.setNome("projeto");
        projeto.setDataInicio(LocalDate.from(of(2025, 1, 1)));
        projeto.setDataPrevisaoFim(LocalDate.from(of(2025, 12, 31)));
        projeto.setOrcamento(1000f);
        projeto.setStatus(Status.EM_ANALISE);

        Projeto projeto2 = new Projeto();
        projeto2.setNome("projeto2");
        projeto2.setDataInicio(LocalDate.from(of(2025, 1, 1)));
        projeto2.setDataPrevisaoFim(LocalDate.from(of(2025, 12, 31)));
        projeto2.setOrcamento(5000f);
        projeto2.setStatus(Status.EM_ANALISE);

        projetoList.add(projeto);
        projetoList.add(projeto2);

        when(projetoRepository.findAll()).thenReturn(projetoList);

        List<Projeto> result = projetoService.findAll();

        assertEquals(projetoList, result);
        verify(projetoRepository, times(1)).findAll();
    }

    @Test
    void findAllException(){
        when(projetoRepository.findAll()).thenThrow(new RuntimeException("Database connection failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> projetoService.findAll());

        assertEquals("Error finding projects: Database connection failed", exception.getMessage());

        verify(projetoRepository, times(1)).findAll();
    }

    @Test
    void findByIdSuccess(){
        Projeto projeto = new Projeto();
        projeto.setId(1L);
        projeto.setNome("projeto");
        projeto.setDataInicio(LocalDate.from(of(2025, 1, 1)));
        projeto.setDataPrevisaoFim(LocalDate.from(of(2025, 12, 31)));
        projeto.setOrcamento(4000f);
        projeto.setStatus(Status.INICIADO);

        when(projetoRepository.findById(projeto.getId())).thenReturn(Optional.of(projeto));

        Projeto result = projetoService.findById(projeto.getId());

        assertEquals(projeto, result);
        verify(projetoRepository, times(1)).findById(projeto.getId());
    }

    @Test
    void findByIdProjetoNotFound(){
        when(projetoRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ProjetoNotFoundException.class, () -> projetoService.findById(2L));

        verify(projetoRepository, times(1)).findById(2L);
    }

    @Test
    void findByIdException(){
        when(projetoRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> projetoService.findById(1L));

        assertEquals("Error finding project: Database error", exception.getMessage());
        verify(projetoRepository, times(1)).findById(1L);
    }

    //=======================TESTES DE ADIÇÃO DE MEMBROS NO PROJETO=======================
    @Test
    void addMembrosToProjetoSuccess(){
        List<Long> idPessoas = Arrays.asList(2L, 3L);

        Pessoa pessoa1 = new Pessoa();
        pessoa1.setId(2L);
        pessoa1.setFuncionario(true);

        Pessoa pessoa2 = new Pessoa();
        pessoa2.setId(3L);
        pessoa2.setFuncionario(true);

        List<Pessoa> pessoas = Arrays.asList(pessoa1, pessoa2);

        Projeto projeto = new Projeto();
        projeto.setId(1L);
        projeto.setMembros(Collections.emptyList());

        when(pessoaRepository.findAllById(idPessoas)).thenReturn(pessoas);
        when(projetoRepository.findById(projeto.getId())).thenReturn(Optional.of(projeto));
        when(projetoRepository.save(any(Projeto.class))).thenReturn(projeto);

        String result = projetoService.addMembrosToProjeto(idPessoas, projeto.getId());

        assertEquals("Membros added to Projeto successfully", result);
        assertEquals(2, projeto.getMembros().size());
        assertTrue(projeto.getMembros().contains(pessoa1));
        assertTrue(projeto.getMembros().contains(pessoa2));

        verify(projetoRepository).save(projeto);
    }

    @Test
    void addMembrosToProjectNoValidEmployee(){
        List<Long> idPessoas = Arrays.asList(2L, 3L);

        Pessoa pessoa1 = new Pessoa();
        pessoa1.setId(2L);
        pessoa1.setFuncionario(false);

        Pessoa pessoa2 = new Pessoa();
        pessoa2.setId(3L);
        pessoa2.setFuncionario(false);

        List<Pessoa> pessoas = Arrays.asList(pessoa1, pessoa2);

        when(pessoaRepository.findAllById(idPessoas)).thenReturn(pessoas);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            projetoService.addMembrosToProjeto(idPessoas, 1L);
        });

        assertEquals("Error vinculating Membros to Projeto: No valid employees found in the provided list.", exception.getMessage());
        verify(projetoRepository, never()).save(any(Projeto.class));
    }

    @Test
    void addMembrosToProjetoProjetoNotFound(){
        List<Long> idPessoas = Arrays.asList(2L);

        Pessoa pessoa = new Pessoa();
        pessoa.setId(2L);
        pessoa.setFuncionario(true);

        when(pessoaRepository.findAllById(idPessoas)).thenReturn(Collections.singletonList(pessoa));
        when(projetoRepository.findById(1L)).thenReturn(Optional.empty());

        ProjetoNotFoundException exception = assertThrows(ProjetoNotFoundException.class, () -> {
            projetoService.addMembrosToProjeto(idPessoas, 1L);
        });

        assertNotNull(exception);
        verify(projetoRepository, never()).save(any(Projeto.class));
    }

    @Test
    void addMembrosToProjetoException(){
        List<Long> idPessoas = Arrays.asList(2L);

        Pessoa pessoa = new Pessoa();
        pessoa.setId(2L);
        pessoa.setFuncionario(true);

        Projeto projeto = new Projeto();
        projeto.setId(1L);
        projeto.setMembros(Collections.emptyList());

        when(pessoaRepository.findAllById(idPessoas)).thenReturn(Collections.singletonList(pessoa));
        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(projetoRepository.save(any(Projeto.class))).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            projetoService.addMembrosToProjeto(idPessoas, 1L);
        });

        assertEquals("Error vinculating Membros to Projeto: Database error", exception.getMessage());
    }

    @Test
    void addMembrosToProjetoOnlyOneEmployee(){
        List<Long> idPessoas = Arrays.asList(2L, 3L);

        Pessoa pessoa1 = new Pessoa();
        pessoa1.setId(2L);
        pessoa1.setFuncionario(false);

        Pessoa pessoa2 = new Pessoa();
        pessoa2.setId(3L);
        pessoa2.setFuncionario(true);

        List<Pessoa> pessoas = Arrays.asList(pessoa1, pessoa2);

        Projeto projeto = new Projeto();
        projeto.setId(1L);
        projeto.setMembros(Collections.emptyList());

        when(pessoaRepository.findAllById(idPessoas)).thenReturn(pessoas);
        when(projetoRepository.findById(projeto.getId())).thenReturn(Optional.of(projeto));
        when(projetoRepository.save(any(Projeto.class))).thenReturn(projeto);

        String result = projetoService.addMembrosToProjeto(idPessoas, projeto.getId());

        assertEquals("Membros added to Projeto successfully", result);
        assertEquals(1, projeto.getMembros().size());
        assertFalse(projeto.getMembros().contains(pessoa1));
        assertTrue(projeto.getMembros().contains(pessoa2));

        verify(projetoRepository).save(projeto);
    }

    @Test
    void addMembrosToProjetoNoExistentId(){
        List<Long> idPessoas = Arrays.asList(2L, 3L);

        Pessoa pessoa1 = new Pessoa();
        pessoa1.setId(2L);
        pessoa1.setFuncionario(true);

        List<Pessoa> pessoas = Collections.singletonList(pessoa1);

        Projeto projeto = new Projeto();
        projeto.setId(1L);
        projeto.setMembros(Collections.emptyList());

        when(pessoaRepository.findAllById(idPessoas)).thenReturn(pessoas);
        when(projetoRepository.findById(projeto.getId())).thenReturn(Optional.of(projeto));

        RuntimeException exception = assertThrows(PessoaNotFoundException.class,
                () -> projetoService.addMembrosToProjeto(idPessoas, projeto.getId())
        );

        verify(projetoRepository, never()).save(any(Projeto.class));
    }
}