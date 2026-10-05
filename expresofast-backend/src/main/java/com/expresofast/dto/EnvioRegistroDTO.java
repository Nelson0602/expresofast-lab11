package com.expresofast.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Datos que llegan desde Angular para registrar un envío junto con todos sus paquetes
public class EnvioRegistroDTO {

    @NotBlank(message = "El número de rastreo es obligatorio")
    @Size(max = 30, message = "El número de rastreo admite máximo 30 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "El número de rastreo solo admite letras, números y guiones")
    private String numeroTracking;

    @NotBlank(message = "El destinatario es obligatorio")
    @Size(max = 120, message = "El destinatario admite máximo 120 caracteres")
    private String destinatario;

    @NotBlank(message = "La dirección de destino es obligatoria")
    @Size(max = 250, message = "La dirección admite máximo 250 caracteres")
    private String direccionDestino;

    @NotNull(message = "El monto del flete es obligatorio")
    @Positive(message = "El monto del flete debe ser mayor a cero")
    private Double montoFlete;

    @NotNull(message = "La fecha de despacho es obligatoria")
    private LocalDate fechaDespacho;

    @NotNull(message = "La fecha de entrega estimada es obligatoria")
    private LocalDate fechaEntregaEstimada;

    @NotEmpty(message = "El envío debe incluir al menos un paquete")
    @Valid
    private List<PaqueteDTO> paquetes = new ArrayList<>();

    public EnvioRegistroDTO() {
    }

    public String getNumeroTracking() {
        return numeroTracking;
    }

    public void setNumeroTracking(String numeroTracking) {
        this.numeroTracking = numeroTracking;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getDireccionDestino() {
        return direccionDestino;
    }

    public void setDireccionDestino(String direccionDestino) {
        this.direccionDestino = direccionDestino;
    }

    public Double getMontoFlete() {
        return montoFlete;
    }

    public void setMontoFlete(Double montoFlete) {
        this.montoFlete = montoFlete;
    }

    public LocalDate getFechaDespacho() {
        return fechaDespacho;
    }

    public void setFechaDespacho(LocalDate fechaDespacho) {
        this.fechaDespacho = fechaDespacho;
    }

    public LocalDate getFechaEntregaEstimada() {
        return fechaEntregaEstimada;
    }

    public void setFechaEntregaEstimada(LocalDate fechaEntregaEstimada) {
        this.fechaEntregaEstimada = fechaEntregaEstimada;
    }

    public List<PaqueteDTO> getPaquetes() {
        return paquetes;
    }

    public void setPaquetes(List<PaqueteDTO> paquetes) {
        this.paquetes = paquetes;
    }
}