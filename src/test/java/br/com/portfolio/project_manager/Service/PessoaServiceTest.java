package br.com.portfolio.project_manager.Service;

import br.com.portfolio.project_manager.Exception.Pessoa.PessoaNotFoundException;
import br.com.portfolio.project_manager.Model.Pessoa;
import br.com.portfolio.project_manager.Repository.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
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
}