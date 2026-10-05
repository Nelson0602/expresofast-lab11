package com.expresofast.repository;

import com.expresofast.model.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// Acceso a datos de los envíos, con búsquedas por código y por estado
public interface EnvioRepository extends JpaRepository<Envio, Long> {

    Optional<Envio> findByCodigoRastreo(String codigoRastreo);

    boolean existsByCodigoRastreo(String codigoRastreo);

    // Consulta JPQL que filtra por estado, del más nuevo al más antiguo
    @Query("SELECT e FROM Envio e WHERE e.estado = :estado ORDER BY e.id DESC")
    List<Envio> searchByStatus(@Param("estado") String estado);
}