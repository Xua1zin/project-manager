package br.com.portfolio.project_manager.Service;

import br.com.portfolio.project_manager.Model.Pessoa;
import br.com.portfolio.project_manager.Repository.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

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
}