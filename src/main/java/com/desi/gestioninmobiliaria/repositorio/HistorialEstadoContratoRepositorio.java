package com.desi.gestioninmobiliaria.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desi.gestioninmobiliaria.modelo.HistorialEstadoContrato;

public interface HistorialEstadoContratoRepositorio extends JpaRepository<HistorialEstadoContrato, Long> {
}