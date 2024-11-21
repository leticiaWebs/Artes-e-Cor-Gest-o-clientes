package com.artesecor.api_gestaoclientes.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ClienteStatus {
    ATIVO("ativo"),
    INATIVO("inativo"),
    CANCELADO("cancelado");

    private String status;

    ClienteStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @JsonCreator
    public static ClienteStatus fromValue(String value) {
        for (ClienteStatus clienteStatus : ClienteStatus.values()) {
            if (clienteStatus.status.equalsIgnoreCase(value)) {
                return clienteStatus;
            }
        }
        throw new IllegalArgumentException("Status inválido: " + value);
    }
}
