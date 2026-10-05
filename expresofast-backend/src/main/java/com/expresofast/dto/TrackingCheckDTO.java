package com.expresofast.dto;

// Respuesta de la consulta asíncrona: indica si el número de rastreo ya existe
public class TrackingCheckDTO {

    private boolean exists;

    public TrackingCheckDTO() {
    }

    public TrackingCheckDTO(boolean exists) {
        this.exists = exists;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }
}