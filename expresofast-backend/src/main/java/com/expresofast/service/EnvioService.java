package com.expresofast.service;

import com.expresofast.dto.EnvioDTO;
import com.expresofast.dto.EnvioRegistroDTO;

import java.util.List;

// Operaciones de negocio disponibles para los envíos
public interface EnvioService {

    List<EnvioDTO> findAll();

    List<EnvioDTO> findByStatus(String estado);

    EnvioDTO findByTrackingCode(String trackingCode);

    EnvioDTO register(EnvioRegistroDTO request);

    EnvioDTO updateStatus(Long id, String newStatus);

    boolean trackingExists(String trackingNumber);
}