package com.artesecor.api_gestaoclientes.infrastructure.repository;

import com.artesecor.api_gestaoclientes.domain.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findById(Long id);

}
