package com.artesecor.api_gestaoclientes.application.service;

import com.artesecor.api_gestaoclientes.application.dto.ClienteDTO;
import com.artesecor.api_gestaoclientes.delivery.handler.DatabaseException;
import com.artesecor.api_gestaoclientes.delivery.handler.DuplicateResourceException;
import com.artesecor.api_gestaoclientes.delivery.handler.ResourceNotFoundException;
import com.artesecor.api_gestaoclientes.domain.model.Cliente;
import com.artesecor.api_gestaoclientes.infrastructure.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    @Transactional
    public List<ClienteDTO> findAll() {
        List<Cliente> list = repository.findAll();
        return list.stream().map(ClienteDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public ClienteDTO insert(ClienteDTO dto) {
        if (repository.existsById(dto.getId())) {
            throw new DuplicateResourceException("Este identificador já existe em nosso sistema: " + dto.getId());
        }
        Cliente entity = new Cliente();
        entity.setNome(dto.getNome());
        entity.setContato(dto.getContato());
        entity.setStatus(dto.getStatus());
        entity = repository.save(entity); // Persistindo no banco
        return new ClienteDTO(entity);   // Retornando o DTO
    }

    @Transactional
    public ClienteDTO findById(String id) {
        Optional<Cliente> obj = repository.findById(id);
        Cliente entity = obj.orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        return new ClienteDTO(entity);
    }

    @Transactional
    public ClienteDTO update(String id, ClienteDTO dto) {
        try {
            Cliente entity = repository.getReferenceById(id);
            entity.setStatus(dto.getStatus());
            entity.setNome(dto.getNome());
            entity.setContato(dto.getContato());
            entity = repository.save(entity);
            return new ClienteDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Identificador do cliente não encontrado" + id);
        }
    }

    public void delete(String id){
        try{
            repository.deleteById(id);
        } catch(EmptyResultDataAccessException e){
            throw  new ResourceNotFoundException("Cliente não encontrado");
        }
        catch(DataIntegrityViolationException e){
            throw new DatabaseException("Integrity violation");
        }
    }
}
