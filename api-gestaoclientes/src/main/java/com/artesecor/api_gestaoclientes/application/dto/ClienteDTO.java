package com.artesecor.api_gestaoclientes.application.dto;

import com.artesecor.api_gestaoclientes.domain.model.Cliente;
import com.artesecor.api_gestaoclientes.domain.model.enums.ClienteStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class ClienteDTO implements Serializable {

    private String id;
    private String nome;
    private String contato;
    private ClienteStatus status;

    public ClienteDTO(String id, String nome, String contato, ClienteStatus status){
        this.id = id;
        this.nome = nome;
        this.contato = contato;
        this.status = status;
    }
    public ClienteDTO(Cliente entity){
        this.id = entity.getId();
        this.nome = entity.getNome();
        this.contato = entity.getContato();
        this.status = entity.getStatus();
    }
}
