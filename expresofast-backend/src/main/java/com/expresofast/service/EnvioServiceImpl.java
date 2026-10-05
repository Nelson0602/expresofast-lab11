package com.expresofast.service;

import com.expresofast.dto.EnvioDTO;
import com.expresofast.dto.EnvioRegistroDTO;
import com.expresofast.dto.PaqueteDTO;
import com.expresofast.model.Envio;
import com.expresofast.model.Paquete;
import com.expresofast.repository.EnvioRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

// Lógica de negocio de los envíos: consulta, registro con paquetes y cambio de estado
@Service
@Transactional(readOnly = true)
public class EnvioServiceImpl implements EnvioService {

    private static final List<String> VALID_STATES = List.of("PENDIENTE", "EN_TRANSITO", "ENTREGADO", "CANCELADO");

    private final EnvioRepository envioRepository;

    public EnvioServiceImpl(EnvioRepository envioRepository) {
        this.envioRepository = envioRepository;
    }

    @Override
    public List<EnvioDTO> findAll() {
        return envioRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<EnvioDTO> findByStatus(String estado) {
        String status = checkStatus(estado);
        return envioRepository.searchByStatus(status)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public EnvioDTO findByTrackingCode(String trackingCode) {
        String code = trackingCode.trim().toUpperCase();
        Envio shipment = envioRepository.findByCodigoRastreo(code)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe un envío con el código " + code));
        return toDTO(shipment);
    }

    // Guarda el envío y todos sus paquetes en una sola transacción
    @Override
    @Transactional
    public EnvioDTO register(EnvioRegistroDTO request) {
        String code = request.getNumeroTracking().trim().toUpperCase();

        if (envioRepository.existsByCodigoRastreo(code)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este número de rastreo ya está en uso");
        }

        if (!request.getFechaEntregaEstimada().isAfter(request.getFechaDespacho())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La fecha de entrega estimada debe ser posterior a la fecha de despacho");
        }

        Envio shipment = new Envio();
        shipment.setCodigoRastreo(code);
        shipment.setDestinatario(request.getDestinatario().trim());
        shipment.setDireccionDestino(request.getDireccionDestino().trim());
        shipment.setMontoFlete(request.getMontoFlete());
        shipment.setEstado("PENDIENTE");
        shipment.setFechaCreacion(LocalDateTime.now());
        shipment.setFechaDespacho(request.getFechaDespacho());
        shipment.setFechaEntregaEstimada(request.getFechaEntregaEstimada());

        for (PaqueteDTO item : request.getPaquetes()) {
            Paquete packageItem = new Paquete();
            packageItem.setDescripcion(item.getDescripcion().trim());
            packageItem.setPesoKg(item.getPesoKg());
            shipment.addPackage(packageItem);
        }

        return toDTO(envioRepository.save(shipment));
    }

    @Override
    @Transactional
    public EnvioDTO updateStatus(Long id, String newStatus) {
        String status = checkStatus(newStatus);
        Envio shipment = envioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe un envío con el id " + id));
        shipment.setEstado(status);
        return toDTO(envioRepository.save(shipment));
    }

    // Dice si el número de rastreo ya existe en la base de datos
    @Override
    public boolean trackingExists(String trackingNumber) {
        return envioRepository.existsByCodigoRastreo(trackingNumber.trim().toUpperCase());
    }

    // Confirma que el estado sea uno de los cuatro permitidos
    private String checkStatus(String status) {
        if (status == null || !VALID_STATES.contains(status.trim().toUpperCase())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Estado no válido. Use: PENDIENTE, EN_TRANSITO, ENTREGADO o CANCELADO");
        }
        return status.trim().toUpperCase();
    }

    // Convierte el envío y sus paquetes a DTO para devolverlos al cliente
    private EnvioDTO toDTO(Envio shipment) {
        EnvioDTO dto = new EnvioDTO();
        dto.setId(shipment.getId());
        dto.setCodigoRastreo(shipment.getCodigoRastreo());
        dto.setDestinatario(shipment.getDestinatario());
        dto.setDireccionDestino(shipment.getDireccionDestino());
        dto.setMontoFlete(shipment.getMontoFlete());
        dto.setEstado(shipment.getEstado());
        dto.setFechaCreacion(shipment.getFechaCreacion());
        dto.setFechaDespacho(shipment.getFechaDespacho());
        dto.setFechaEntregaEstimada(shipment.getFechaEntregaEstimada());
        dto.setPaquetes(shipment.getPaquetes().stream().map(this::toPackageDTO).toList());
        return dto;
    }

    private PaqueteDTO toPackageDTO(Paquete packageItem) {
        PaqueteDTO dto = new PaqueteDTO();
        dto.setId(packageItem.getId());
        dto.setDescripcion(packageItem.getDescripcion());
        dto.setPesoKg(packageItem.getPesoKg());
        return dto;
    }
}