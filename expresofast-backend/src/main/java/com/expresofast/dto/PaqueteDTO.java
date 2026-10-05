package com.expresofast.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

// Datos de un paquete: llegan en el POST y también se devuelven al cliente
public class PaqueteDTO {

    private Long id;

    @NotBlank(message = "La descripción del paquete es obligatoria")
    @Size(max = 255, message = "La descripción admite máximo 255 caracteres")
    private String descripcion;

    @NotNull(message = "El peso del paquete es obligatorio")
    @DecimalMin(value = "0.01", message = "El peso debe ser mayor a cero")
    @DecimalMax(value = "999.99", message = "El peso no puede superar 999.99 kg")
    @Digits(integer = 3, fraction = 2, message = "El peso admite hasta 2 decimales")
    private BigDecimal pesoKg;

    public PaqueteDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(BigDecimal pesoKg) {
        this.pesoKg = pesoKg;
    }
}