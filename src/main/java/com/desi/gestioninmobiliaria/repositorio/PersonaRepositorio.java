package com.desi.gestioninmobiliaria.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desi.gestioninmobiliaria.modelo.Persona;

public interface PersonaRepositorio extends JpaRepository<Persona, Long> {

    List<Persona> findByEliminadoFalse();

}