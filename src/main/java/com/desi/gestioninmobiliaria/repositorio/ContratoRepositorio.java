package com.desi.gestioninmobiliaria.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desi.gestioninmobiliaria.enums.EstadoContrato;
import com.desi.gestioninmobiliaria.modelo.Contrato;
import com.desi.gestioninmobiliaria.modelo.Persona;
import com.desi.gestioninmobiliaria.modelo.Propiedad;

public interface ContratoRepositorio extends JpaRepository<Contrato, Long> {

    List<Contrato> findByEliminadoFalse();

    Optional<Contrato> findByIdAndEliminadoFalse(Long id);

    List<Contrato> findByEstadoContratoAndEliminadoFalse(EstadoContrato estadoContrato);

    List<Contrato> findByPropiedadAndEliminadoFalse(Propiedad propiedad);

    Optional<Contrato> findByPropiedadAndEstadoContratoAndEliminadoFalse(
            Propiedad propiedad,
            EstadoContrato estadoContrato);

    List<Contrato> findByInquilinoAndEliminadoFalse(Persona inquilino);

    boolean existsByPropiedadAndEstadoContratoAndEliminadoFalse(
            Propiedad propiedad,
            EstadoContrato estadoContrato);

    boolean existsByPropiedadAndEstadoContratoAndEliminadoFalseAndIdNot(
            Propiedad propiedad,
            EstadoContrato estadoContrato,
            Long id);
}