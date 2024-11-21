package com.artesecor.api_gestaoclientes.domain.model;

import com.artesecor.api_gestaoclientes.domain.model.enums.ClienteStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy =  GenerationType.UUID)
    private String id;
    public String nome;
    private String contato;
    @Enumerated(EnumType.STRING)
    private ClienteStatus status;
}
