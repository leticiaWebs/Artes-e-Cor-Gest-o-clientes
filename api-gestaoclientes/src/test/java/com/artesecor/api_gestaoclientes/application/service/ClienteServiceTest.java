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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        Cliente cliente2 = new Cliente(2L, "Cliente 2", "5678", ClienteStatus.INATIVO);
        when(repository.findAll()).thenReturn(Arrays.asList(cliente, cliente2));


        var result = service.findAll();


        assertEquals(2, result.size());
        assertEquals("Cliente 1", result.get(0).getNome());
        assertEquals("Cliente 2", result.get(1).getNome());
    }

    @Test
    void testInsert_Success() {
        when(repository.existsById(clienteDTO.getId())).thenReturn(false);
        when(repository.save(any(Cliente.class))).thenReturn(cliente);

        ClienteDTO result = service.insert(clienteDTO);

        verify(repository).save(any(Cliente.class));
        assertEquals(clienteDTO.getNome(), result.getNome());
        assertEquals(clienteDTO.getContato(), result.getContato());
    }

    @Test
    void testInsert_AlreadyExists() {
        when(repository.existsById(clienteDTO.getId())).thenReturn(true);

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
            service.insert(clienteDTO);
        });

        assertEquals("Este identificador já existe em nosso sistema: 1", exception.getMessage());
    }

    @Test
    void testFindById_Success() {
        when(repository.findById(clienteDTO.getId())).thenReturn(Optional.of(cliente));


        ClienteDTO result = service.findById(clienteDTO.getId());

        assertEquals(clienteDTO.getNome(), result.getNome());
        assertEquals(clienteDTO.getContato(), result.getContato());
    }

    @Test
    void testFindById_NotFound() {

        when(repository.findById(clienteDTO.getId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(clienteDTO.getId());
        });

        assertEquals("Cliente não encontrado", exception.getMessage());
    }

    @Test
    void testUpdate_Success() {

        when(repository.getReferenceById(clienteDTO.getId())).thenReturn(cliente);
        when(repository.save(any(Cliente.class))).thenReturn(cliente);


        ClienteDTO result = service.update(clienteDTO.getId(), clienteDTO);

        assertEquals(clienteDTO.getNome(), result.getNome());
        assertEquals(clienteDTO.getContato(), result.getContato());
    }

    @Test
    void testUpdate_NotFound() {

        when(repository.getReferenceById(clienteDTO.getId())).thenThrow(EntityNotFoundException.class);

        // Chamar o método e verificar a exceção
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.update(clienteDTO.getId(), clienteDTO);
        });


        assertEquals("Identificador do cliente não encontrado1", exception.getMessage());
    }

    @Test
    void testDelete_Success() {

        doNothing().when(repository).deleteById(clienteDTO.getId());


        service.delete(clienteDTO.getId());


        verify(repository).deleteById(clienteDTO.getId());
    }

    @Test
    void testDelete_NotFound() {

        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(clienteDTO.getId());


        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(clienteDTO.getId());
        });


        assertEquals("Cliente não encontrado", exception.getMessage());
    }

    @Test
    void testDelete_DataIntegrityViolation() {

        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(clienteDTO.getId());


        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            service.delete(clienteDTO.getId());
        });


        assertEquals("Integrity violation", exception.getMessage());
    }
}
