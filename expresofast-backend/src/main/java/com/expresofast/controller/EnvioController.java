package com.expresofast.controller;

import com.expresofast.dto.EnvioDTO;
import com.expresofast.dto.EnvioRegistroDTO;
import com.expresofast.dto.TrackingCheckDTO;
import com.expresofast.dto.UpdateStatusDTO;
import com.expresofast.service.EnvioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Controlador REST de envíos, con CORS abierto para Angular
@RestController
@RequestMapping("/api/v1/envios")
@CrossOrigin(origins = "http://localhost:4200")
public class EnvioController {

    private final EnvioService envioService;

    public EnvioController(EnvioService envioService) {
        this.envioService = envioService;
    }

    // Lista todos los envíos, o solo los de un estado si se envía ?estado=
    @GetMapping
    public List<EnvioDTO> getAll(@RequestParam(required = false) String estado) {
        if (estado == null || estado.isBlank()) {
            return envioService.findAll();
        }
        return envioService.findByStatus(estado);
    }

    @GetMapping("/rastreo/{codigo}")
    public EnvioDTO getByTrackingCode(@PathVariable String codigo) {
        return envioService.findByTrackingCode(codigo);
    }

    // Consulta que usa el validador asíncrono de Angular para evitar rastreos repetidos
    @GetMapping("/check-tracking/{trackingNumber}")
    public TrackingCheckDTO checkTracking(@PathVariable String trackingNumber) {
        return new TrackingCheckDTO(envioService.trackingExists(trackingNumber));
    }

    // Registra el envío junto con su lista de paquetes
    @PostMapping
    public ResponseEntity<EnvioDTO> create(@Valid @RequestBody EnvioRegistroDTO request) {
        EnvioDTO created = envioService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}/estado")
    public EnvioDTO changeStatus(@PathVariable Long id, @RequestBody UpdateStatusDTO body) {
        return envioService.updateStatus(id, body.getEstado());
    }
}