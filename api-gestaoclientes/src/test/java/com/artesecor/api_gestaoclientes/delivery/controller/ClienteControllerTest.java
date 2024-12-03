package com.artesecor.api_gestaoclientes.delivery.controller;

import com.artesecor.api_gestaoclientes.application.dto.ClienteDTO;
import com.artesecor.api_gestaoclientes.application.service.ClienteService;
import com.artesecor.api_gestaoclientes.domain.model.enums.ClienteStatus;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService service;

    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        clienteDTO = new ClienteDTO(1L, "Cliente 1", "1234", ClienteStatus.ATIVO);
    }

    @Test
    void testFindAll() throws Exception {
        // Preparar o cenário
        when(service.findAll()).thenReturn(List.of(clienteDTO));

        // Chamar o endpoint
        mockMvc.perform(MockMvcRequestBuilders.get("/clientes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(clienteDTO.getId()))
                .andExpect(jsonPath("$[0].nome").value(clienteDTO.getNome()))
                .andExpect(jsonPath("$[0].contato").value(clienteDTO.getContato()))
                .andExpect(jsonPath("$[0].status").value(clienteDTO.getStatus().toString()));

        // Verificar se o método foi chamado
        verify(service, times(1)).findAll();
    }

    @Test
    void testFindById() throws Exception {
        // Preparar o cenário
        when(service.findById(1L)).thenReturn(clienteDTO);

        // Chamar o endpoint
        mockMvc.perform(MockMvcRequestBuilders.get("/clientes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clienteDTO.getId()))
                .andExpect(jsonPath("$.nome").value(clienteDTO.getNome()))
                .andExpect(jsonPath("$.contato").value(clienteDTO.getContato()))
                .andExpect(jsonPath("$.status").value(clienteDTO.getStatus().toString()));

        // Verificar se o método foi chamado
        verify(service, times(1)).findById(1L);
    }

    @Test
    void testInsert() throws Exception {
        // Preparar o cenário
        when(service.insert(Mockito.any(ClienteDTO.class))).thenReturn(clienteDTO);

        // Chamar o endpoint
        mockMvc.perform(MockMvcRequestBuilders.post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(clienteDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/clientes/1"))
                .andExpect(jsonPath("$.id").value(clienteDTO.getId()))
                .andExpect(jsonPath("$.nome").value(clienteDTO.getNome()))
                .andExpect(jsonPath("$.contato").value(clienteDTO.getContato()))
                .andExpect(jsonPath("$.status").value(clienteDTO.getStatus().toString()));

        // Verificar se o método foi chamado
        verify(service, times(1)).insert(Mockito.any(ClienteDTO.class));
    }

    @Test
    void testUpdate() throws Exception {
        // Preparar o cenário
        when(service.update(eq(1L), Mockito.any(ClienteDTO.class))).thenReturn(clienteDTO);

        // Chamar o endpoint
        mockMvc.perform(MockMvcRequestBuilders.put("/clientes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(clienteDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clienteDTO.getId()))
                .andExpect(jsonPath("$.nome").value(clienteDTO.getNome()))
                .andExpect(jsonPath("$.contato").value(clienteDTO.getContato()))
                .andExpect(jsonPath("$.status").value(clienteDTO.getStatus().toString()));

        // Verificar se o método foi chamado
        verify(service, times(1)).update(eq(1L), Mockito.any(ClienteDTO.class));
    }

    @Test
    void testDelete() throws Exception {
        // Preparar o cenário
        doNothing().when(service).delete(1L);

        // Chamar o endpoint
        mockMvc.perform(MockMvcRequestBuilders.delete("/clientes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verificar se o método foi chamado
        verify(service, times(1)).delete(1L);
    }

    // Método auxiliar para converter objeto em JSON
    public static String asJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
