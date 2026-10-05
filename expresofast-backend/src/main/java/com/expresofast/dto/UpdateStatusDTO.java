package com.expresofast.dto;

// Cuerpo de la petición PATCH que trae el nuevo estado del envío
public class UpdateStatusDTO {

    private String estado;

    public UpdateStatusDTO() {
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}