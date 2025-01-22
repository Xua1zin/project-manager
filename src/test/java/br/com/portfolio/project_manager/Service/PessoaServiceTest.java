package br.com.portfolio.project_manager.Service;

import br.com.portfolio.project_manager.Exception.Pessoa.PessoaNotFoundException;
import br.com.portfolio.project_manager.Model.Pessoa;
import br.com.portfolio.project_manager.Model.Projeto;
import br.com.portfolio.project_manager.Repository.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PessoaServiceTest {
    @InjectMocks
    PessoaService pessoaService;

    @Mock
    PessoaRepository pessoaRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //=======================TESTES DE SAVE=======================
    @Test
    void saveSuccess() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("teste");
        pessoa.setCpf("111.111.111-11");
        pessoa.setDataNascimento(LocalDate.of(1995, 12, 2));

        when(pessoaRepository.save(pessoa)).thenReturn(pessoa);
        String result = pessoaService.save(pessoa);

        assertEquals("Person created successfully", result);
        verify(pessoaRepository, times(1)).save(pessoa);
    }

    @Test
    void saveException() {
        Pessoa pessoa = new Pessoa();
        doThrow(new RuntimeException("Database error")).when(pessoaRepository).save(pessoa);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> pessoaService.save(pessoa));
        assertEquals("Error saving person: Database error", exception.getMessage());
    }

    //=======================TESTES DE DELETE=======================
    @Test
    void deleteSuccess(){
        Pessoa pessoa = new Pessoa();
        pessoa.setId(2L);
        pessoa.setNome("teste");
        pessoa.setCpf("111.111.111-11");
        pessoa.setDataNascimento(LocalDate.of(1995, 12, 2));

        when(pessoaRepository.findById(pessoa.getId())).thenReturn(Optional.of(pessoa));
        doNothing().when(pessoaRepository).deleteById(pessoa.getId());

        String result = pessoaService.delete(pessoa.getId());
        assertEquals("Person successfully deleted", result);

        verify(pessoaRepository,times(1)).deleteById(pessoa.getId());
    }

    @Test
    void deletePessoaNotFound(){
        Long id = 2L;
        when(pessoaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PessoaNotFoundException.class, () -> pessoaService.delete(id));

        verify(pessoaRepository, never()).deleteById(id);
    }

    @Test
    void deleteLinkedProjects(){
        Long id = 1L;
        Pessoa pessoa = new Pessoa();
        pessoa.setId(id);
        pessoa.setProjetos(new ArrayList<>());

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoa));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            pessoaService.delete(id);
        });
        assertEquals("People who are linked to project cannot be deleted", exception.getMessage());
    }

    @Test
    public void deleteException() {
        Long id = 1L;
        Pessoa pessoa = new Pessoa();
        pessoa.setId(id);

        when(pessoaRepository.findById(id)).thenThrow(new RuntimeException("Unexpected error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pessoaService.delete(id);
        });
        assertEquals("Error deleting person: Unexpected error", exception.getMessage());
    }

    //=======================TESTES DE UPDATE=======================
    @Test
    public void updateSuccess() {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("João");
        pessoa.setCpf("12345678900");
        pessoa.setDataNascimento(LocalDate.of(1995, 5, 10));
        pessoa.setGerente(false);
        pessoa.setFuncionario(true);

        Pessoa updatedPessoa = new Pessoa();
        updatedPessoa.setNome("Carlos");
        updatedPessoa.setCpf("09876543211");
        updatedPessoa.setDataNascimento(LocalDate.of(1992, 3, 20));
        updatedPessoa.setGerente(true);
        updatedPessoa.setFuncionario(true);

        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(updatedPessoa);

        String response = pessoaService.update(updatedPessoa, 1L);

        assertEquals("Person successfully updated", response);
        assertEquals("Carlos", pessoa.getNome());
        assertEquals("09876543211", pessoa.getCpf());

        verify(pessoaRepository, times(1)).findById(1L);
        verify(pessoaRepository, times(1)).save(pessoa);
    }

    @Test
    public void updatePessoaNotFound() {
        Pessoa updatedPessoa = new Pessoa();

        when(pessoaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PessoaNotFoundException.class, () -> pessoaService.update(updatedPessoa, 1L));

        verify(pessoaRepository, never()).save(updatedPessoa);
    }

    @Test
    public void updateException() {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("João");
        pessoa.setCpf("12345678900");
        pessoa.setDataNascimento(LocalDate.of(1995, 5, 10));
        pessoa.setGerente(false);
        pessoa.setFuncionario(true);

        Pessoa updatedPessoa = new Pessoa();
        updatedPessoa.setNome("Carlos");
        updatedPessoa.setCpf("09876543211");
        updatedPessoa.setDataNascimento(LocalDate.of(1992, 3, 20));
        updatedPessoa.setGerente(true);
        updatedPessoa.setFuncionario(true);

        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        when(pessoaRepository.save(any(Pessoa.class))).thenThrow(new RuntimeException("Database error"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> pessoaService.update(updatedPessoa, 1L));
        assertEquals("Error updating person: Database error", thrown.getMessage());
        verify(pessoaRepository, never()).save(updatedPessoa);
    }

    //=======================TESTES DE CONSULTA=======================
    @Test
    void findByIdSuccess(){
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("Teste");
        pessoa.setCpf("12345678900");
        pessoa.setDataNascimento(LocalDate.of(1995, 5, 10));
        pessoa.setGerente(false);
        pessoa.setFuncionario(true);

        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));

        Pessoa result = pessoaService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Teste", result.getNome());
        verify(pessoaRepository, times(1)).findById(1L);
    }

    @Test
    public void findByIdPessoaNotFound() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PessoaNotFoundException.class, () -> pessoaService.findById(1L));
        verify(pessoaRepository, times(1)).findById(1L);
    }

    @Test
    public void findByIdException() {
        when(pessoaRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> pessoaService.findById(1L));
        assertEquals("Error finding person: Database error", exception.getMessage());
        verify(pessoaRepository, times(1)).findById(1L);
    }

    @Test
    public void findAllSuccess() {
        Pessoa pessoa1 = new Pessoa();
        pessoa1.setId(1L);
        pessoa1.setNome("pessoa1");

        Pessoa pessoa2 = new Pessoa();
        pessoa2.setId(2L);
        pessoa2.setNome("pessoa2");
        List<Pessoa> pessoas = Arrays.asList(pessoa1, pessoa2);
        when(pessoaRepository.findAll()).thenReturn(pessoas);

        List<Pessoa> result = pessoaService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(pessoa1));
        assertTrue(result.contains(pessoa2));
        verify(pessoaRepository,times(1)).findAll();
    }

    @Test
    public void findAllException() {
        when(pessoaRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> pessoaService.findAll());
        assertEquals("Error finding people: Database error", thrown.getMessage());
        verify(pessoaRepository,times(1)).findAll();
    }
}