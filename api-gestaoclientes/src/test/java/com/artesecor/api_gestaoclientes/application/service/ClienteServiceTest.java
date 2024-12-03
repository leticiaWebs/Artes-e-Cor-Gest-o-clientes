package com.artesecor.api_gestaoclientes.application.service;
import com.artesecor.api_gestaoclientes.application.dto.ClienteDTO;
import com.artesecor.api_gestaoclientes.delivery.handler.DatabaseException;
import com.artesecor.api_gestaoclientes.delivery.handler.DuplicateResourceException;
import com.artesecor.api_gestaoclientes.delivery.handler.ResourceNotFoundException;
import com.artesecor.api_gestaoclientes.domain.model.Cliente;
import com.artesecor.api_gestaoclientes.domain.model.enums.ClienteStatus;
import com.artesecor.api_gestaoclientes.infrastructure.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import java.util.Arrays;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClienteServiceTest {

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteService service;

    private Cliente cliente;
    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cliente = new Cliente(1L, "Cliente 1", "1234", ClienteStatus.ATIVO);
        clienteDTO = new ClienteDTO(cliente);
    }

    @Test
    void testFindAll() {
        // Preparar o cenário
        Cliente cliente2 = new Cliente(2L, "Cliente 2", "5678", ClienteStatus.INATIVO);
        when(repository.findAll()).thenReturn(Arrays.asList(cliente, cliente2));

        // Chamar o método
        var result = service.findAll();

        // Verificar se a resposta está correta
        assertEquals(2, result.size());
        assertEquals("Cliente 1", result.get(0).getNome());
        assertEquals("Cliente 2", result.get(1).getNome());
    }

    @Test
    void testInsert_Success() {
        // Preparar o cenário
        when(repository.existsById(clienteDTO.getId())).thenReturn(false);
        when(repository.save(any(Cliente.class))).thenReturn(cliente);

        // Chamar o método
        ClienteDTO result = service.insert(clienteDTO);

        // Verificar se o método foi chamado e se a resposta está correta
        verify(repository).save(any(Cliente.class));
        assertEquals(clienteDTO.getNome(), result.getNome());
        assertEquals(clienteDTO.getContato(), result.getContato());
    }

    @Test
    void testInsert_AlreadyExists() {
        // Preparar o cenário
        when(repository.existsById(clienteDTO.getId())).thenReturn(true);

        // Chamar o método e verificar a exceção
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
            service.insert(clienteDTO);
        });

        // Verificar a mensagem da exceção
        assertEquals("Este identificador já existe em nosso sistema: 1", exception.getMessage());
    }

    @Test
    void testFindById_Success() {
        // Preparar o cenário
        when(repository.findById(clienteDTO.getId())).thenReturn(Optional.of(cliente));

        // Chamar o método
        ClienteDTO result = service.findById(clienteDTO.getId());

        // Verificar se a resposta está correta
        assertEquals(clienteDTO.getNome(), result.getNome());
        assertEquals(clienteDTO.getContato(), result.getContato());
    }

    @Test
    void testFindById_NotFound() {
        // Preparar o cenário
        when(repository.findById(clienteDTO.getId())).thenReturn(Optional.empty());

        // Chamar o método e verificar a exceção
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(clienteDTO.getId());
        });

        // Verificar a mensagem da exceção
        assertEquals("Cliente não encontrado", exception.getMessage());
    }

    @Test
    void testUpdate_Success() {
        // Preparar o cenário
        when(repository.getReferenceById(clienteDTO.getId())).thenReturn(cliente);
        when(repository.save(any(Cliente.class))).thenReturn(cliente);

        // Chamar o método
        ClienteDTO result = service.update(clienteDTO.getId(), clienteDTO);

        // Verificar se a resposta está correta
        assertEquals(clienteDTO.getNome(), result.getNome());
        assertEquals(clienteDTO.getContato(), result.getContato());
    }

    @Test
    void testUpdate_NotFound() {
        // Preparar o cenário
        when(repository.getReferenceById(clienteDTO.getId())).thenThrow(EntityNotFoundException.class);

        // Chamar o método e verificar a exceção
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.update(clienteDTO.getId(), clienteDTO);
        });

        // Verificar a mensagem da exceção
        assertEquals("Identificador do cliente não encontrado1", exception.getMessage());
    }

    @Test
    void testDelete_Success() {
        // Preparar o cenário
        doNothing().when(repository).deleteById(clienteDTO.getId());

        // Chamar o método
        service.delete(clienteDTO.getId());

        // Verificar se o repositório foi chamado
        verify(repository).deleteById(clienteDTO.getId());
    }

    @Test
    void testDelete_NotFound() {
        // Preparar o cenário
        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(clienteDTO.getId());

        // Chamar o método e verificar a exceção
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(clienteDTO.getId());
        });

        // Verificar a mensagem da exceção
        assertEquals("Cliente não encontrado", exception.getMessage());
    }

    @Test
    void testDelete_DataIntegrityViolation() {
        // Preparar o cenário
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(clienteDTO.getId());

        // Chamar o método e verificar a exceção
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            service.delete(clienteDTO.getId());
        });

        // Verificar a mensagem da exceção
        assertEquals("Integrity violation", exception.getMessage());
    }
}
