package com.desi.gestioninmobiliaria.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desi.gestioninmobiliaria.modelo.HistorialEstadoPropiedad;

public interface HistorialEstadoPropiedadRepositorio extends JpaRepository<HistorialEstadoPropiedad, Long> {
}
