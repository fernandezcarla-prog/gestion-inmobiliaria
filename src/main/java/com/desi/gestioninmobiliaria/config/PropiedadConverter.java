package com.desi.gestioninmobiliaria.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.desi.gestioninmobiliaria.modelo.Propiedad;
import com.desi.gestioninmobiliaria.repositorio.PropiedadRepositorio;

@Component
public class PropiedadConverter implements Converter<String, Propiedad> {

    private final PropiedadRepositorio propiedadRepositorio;

    public PropiedadConverter(PropiedadRepositorio propiedadRepositorio) {
        this.propiedadRepositorio = propiedadRepositorio;
    }

    @Override
    public Propiedad convert(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }

        return propiedadRepositorio.findById(Long.valueOf(id))
                .orElse(null);
    }
} //Sirve para que Spring entienda qué propiedad seleccionó el usuario. 
// Recibe el id como texto-->Busca esa propiedad en la base de datos y devuelve el objeto Propiedad