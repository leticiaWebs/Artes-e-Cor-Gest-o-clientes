package com.artesecor.api_gestaoclientes.delivery.controller;

import com.artesecor.api_gestaoclientes.application.dto.ClienteDTO;
import com.artesecor.api_gestaoclientes.application.service.ClienteService;
import com.artesecor.api_gestaoclientes.domain.model.enums.ClienteStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


public class ClienteControllerTest {
    @InjectMocks
    private ClienteController controller;

    @Mock
    private ClienteService service;

    private ClienteDTO clienteDTO;

    private List<ClienteDTO> clienteDTOList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        clienteDTO = new ClienteDTO("123e4567-e89b-12d3-a456-426614174000", "João Silva", "joao@teste.com", ClienteStatus.ATIVO);
        clienteDTOList = Arrays.asList(
                new ClienteDTO("123e4567-e89b-12d3-a456-426614174000", "João Silva", "joao@teste.com", ClienteStatus.ATIVO),
                new ClienteDTO("223e4567-e89b-12d3-a456-426614174001", "Maria Souza", "maria@teste.com", ClienteStatus.INATIVO)
        );
    }

    @Test
    void findAll_shouldReturnListOfClientes() {
        when(service.findAll()).thenReturn(clienteDTOList);

        ResponseEntity<List<ClienteDTO>> response = controller.findAll();

        assertNotNull(response);
        assertEquals("Status Code", 200, response.getStatusCodeValue());
        assertEquals("Tamanho da lista", 2, response.getBody().size());
        verify(service, times(1)).findAll();
    }

    @Test
    void findById_shouldReturnClienteDTO_whenIdExists() {
        when(service.findById("123e4567-e89b-12d3-a456-426614174000")).thenReturn(clienteDTO);

        ResponseEntity<ClienteDTO> response = controller.findById("123e4567-e89b-12d3-a456-426614174000");

        assertNotNull(response);
        assertEquals("Status Code", 200, response.getStatusCodeValue());
        assertEquals("Nome do Cliente", "João Silva", response.getBody().getNome());
        verify(service, times(1)).findById("123e4567-e89b-12d3-a456-426614174000");
    }

    @Test
    void update_shouldUpdateClienteAndReturnUpdatedDTO() {
        when(service.update(eq("123e4567-e89b-12d3-a456-426614174000"), any(ClienteDTO.class))).thenReturn(clienteDTO);

        ResponseEntity<ClienteDTO> response = controller.update("123e4567-e89b-12d3-a456-426614174000", clienteDTO);

        assertNotNull(response);
        assertEquals("Status Code", 200, response.getStatusCodeValue());
        assertEquals("Nome do Cliente", "João Silva", response.getBody().getNome());
        verify(service, times(1)).update(eq("123e4567-e89b-12d3-a456-426614174000"), any(ClienteDTO.class));
    }
    @Test
    void insert_shouldCreateClienteAndReturnLocationHeader() {
        when(service.insert(any(ClienteDTO.class))).thenReturn(clienteDTO);

        ResponseEntity<ClienteDTO> response = controller.insert(clienteDTO);

        assertNotNull(response);
        assertEquals("Status Code", 201, response.getStatusCodeValue());
        assertNotNull("URI do Cliente", String.valueOf(response.getHeaders().getLocation()));
        assertEquals("Nome do Cliente", "João Silva", response.getBody().getNome());
        verify(service, times(1)).insert(clienteDTO);
    }

    @Test
    void delete_shouldDeleteClienteAndReturnNoContent() {
        doNothing().when(service).delete("123e4567-e89b-12d3-a456-426614174000");

        ResponseEntity<ClienteDTO> response = controller.delete("123e4567-e89b-12d3-a456-426614174000");

        assertNotNull(response);
        assertEquals("Status Code", 204, response.getStatusCodeValue());
        verify(service, times(1)).delete("123e4567-e89b-12d3-a456-426614174000");
    }
}
