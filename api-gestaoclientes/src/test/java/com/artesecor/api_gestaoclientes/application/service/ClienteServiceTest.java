package com.artesecor.api_gestaoclientes.application.service;

import com.artesecor.api_gestaoclientes.application.dto.ClienteDTO;
import com.artesecor.api_gestaoclientes.delivery.handler.DuplicateResourceException;
import com.artesecor.api_gestaoclientes.delivery.handler.ResourceNotFoundException;
import com.artesecor.api_gestaoclientes.domain.model.Cliente;
import com.artesecor.api_gestaoclientes.domain.model.enums.ClienteStatus;
import com.artesecor.api_gestaoclientes.infrastructure.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ClienteServiceTest {

    @InjectMocks
    private ClienteService service;

    @Mock
    private ClienteRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ShouldReturnListOfClienteDTO() {
        // Arrange
        Cliente cliente1 = new Cliente("223e4567-e89b-12d3-a456-426614174001", "Maria Oliveira", "maria.oliveira@email.com", ClienteStatus.ATIVO);
        Cliente cliente2 = new Cliente("323e4567-e89b-12d3-a456-426614174002", "Carlos Souza", "carlos.souza@email.com", ClienteStatus.INATIVO);
        when(repository.findAll()).thenReturn(Arrays.asList(cliente1, cliente2));

        // Act
        List<ClienteDTO> result = service.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Maria Oliveira", result.get(0).getNome());
        verify(repository, times(1)).findAll();
    }

    @Test
    void insert_ShouldSaveAndReturnClienteDTO() {
        // Arrange
        ClienteDTO dto = new ClienteDTO("223e4567-e89b-12d3-a456-426614174001", "Maria Oliveira", "maria.oliveira@email.com", ClienteStatus.ATIVO);
        Cliente cliente = new Cliente("223e4567-e89b-12d3-a456-426614174001", "Maria Oliveira", "maria.oliveira@email.com", ClienteStatus.ATIVO);
        when(repository.existsById(dto.getId())).thenReturn(false);
        when(repository.save(any(Cliente.class))).thenReturn(cliente);

        // Act
        ClienteDTO result = service.insert(dto);

        // Assert
        assertNotNull(result);
        assertEquals("Maria Oliveira", result.getNome());
        verify(repository, times(1)).save(any(Cliente.class));
    }

    @Test
    void insert_ShouldThrowDuplicateResourceException_WhenIdExists() {
        // Arrange
        ClienteDTO dto = new ClienteDTO("223e4567-e89b-12d3-a456-426614174001", "Maria Oliveira", "maria.oliveira@email.com", ClienteStatus.ATIVO);
        when(repository.existsById(dto.getId())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> service.insert(dto));
        verify(repository, never()).save(any(Cliente.class));
    }

    @Test
    void findById_ShouldReturnClienteDTO() {
        // Arrange
        Cliente cliente = new Cliente("223e4567-e89b-12d3-a456-426614174001", "Maria Oliveira", "maria.oliveira@email.com", ClienteStatus.ATIVO);
        when(repository.findById("223e4567-e89b-12d3-a456-426614174001")).thenReturn(Optional.of(cliente));

        // Act
        ClienteDTO result = service.findById("223e4567-e89b-12d3-a456-426614174001");

        // Assert
        assertNotNull(result);
        assertEquals("Maria Oliveira", result.getNome());
        verify(repository, times(1)).findById("223e4567-e89b-12d3-a456-426614174001");
    }

    @Test
    void findById_ShouldThrowResourceNotFoundException_WhenIdNotFound() {
        // Arrange
        when(repository.findById("223e4567-e89b-12d3-a456-426614174001")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> service.findById("223e4567-e89b-12d3-a456-426614174001"));
        verify(repository, times(1)).findById("223e4567-e89b-12d3-a456-426614174001");
    }

    @Test
    void update_ShouldUpdateAndReturnClienteDTO() {
        // Arrange
        Cliente cliente = new Cliente("223e4567-e89b-12d3-a456-426614174001", "Ana Maria", "Contato 1", ClienteStatus.ATIVO);
        ClienteDTO dto = new ClienteDTO("223e4567-e89b-12d3-a456-426614174001", "Ana Maria Silva", "Contato Atualizado", ClienteStatus.INATIVO);
        when(repository.getReferenceById("223e4567-e89b-12d3-a456-426614174001")).thenReturn(cliente);
        when(repository.save(any(Cliente.class))).thenReturn(cliente);

        // Act
        ClienteDTO result = service.update("223e4567-e89b-12d3-a456-426614174001", dto);

        // Assert
        assertNotNull(result);
        assertEquals("Ana Maria Silva", cliente.getNome());
        verify(repository, times(1)).getReferenceById("223e4567-e89b-12d3-a456-426614174001");
        verify(repository, times(1)).save(cliente);
    }

    @Test
    void delete_ShouldDeleteCliente_WhenIdExists() {
        // Act
        service.delete("223e4567-e89b-12d3-a456-426614174001");

        // Assert
        verify(repository, times(1)).deleteById("223e4567-e89b-12d3-a456-426614174001");
    }

    @Test
    void delete_ShouldThrowResourceNotFoundException_WhenIdNotFound() {
        // Arrange
        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById("223e4567-e89b-12d3-a456-426614174001");

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> service.delete("223e4567-e89b-12d3-a456-426614174001"));
        verify(repository, times(1)).deleteById("223e4567-e89b-12d3-a456-426614174001");
    }
}

