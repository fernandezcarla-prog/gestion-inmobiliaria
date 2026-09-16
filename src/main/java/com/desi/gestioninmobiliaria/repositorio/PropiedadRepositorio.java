package com.desi.gestioninmobiliaria.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desi.gestioninmobiliaria.enums.EstadoDisponibilidad;
import com.desi.gestioninmobiliaria.enums.TipoPropiedad;
import com.desi.gestioninmobiliaria.modelo.Propiedad;

public interface PropiedadRepositorio extends JpaRepository<Propiedad, Long> {

    List<Propiedad> findByEliminadoFalse(); //Buscar todas las propiedades q no esten enliminadas

    Optional<Propiedad> findByDireccionIgnoreCaseAndCiudadIgnoreCaseAndEliminadoFalse(String direccion, String ciudad);
     //Busca una propiedad por dirección y ciudad, sin importar mayúsculas o minúsculas,
    //y que no esté eliminada

    List<Propiedad> findByEstadoDisponibilidadAndEliminadoFalse(EstadoDisponibilidad estadoDisponibilidad);
//Busca propiedades por estad y q no esten eliminada
    List<Propiedad> findByTipoPropiedadAndEliminadoFalse(TipoPropiedad tipoPropiedad);
//busca propiedades por tipo y q no este eliminada
    List<Propiedad> findByCiudadContainingIgnoreCaseAndEliminadoFalse(String ciudad);

    List<Propiedad> findByDireccionContainingIgnoreCaseAndEliminadoFalse(String direccion);
}

//el repositorio es el que busca datos en la base.
//En este caso busca propiedades pero q no esten eliminada logicamente