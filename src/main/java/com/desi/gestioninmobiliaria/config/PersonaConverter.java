package com.desi.gestioninmobiliaria.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.desi.gestioninmobiliaria.modelo.Persona;
import com.desi.gestioninmobiliaria.repositorio.PersonaRepositorio;

@Component
public class PersonaConverter implements Converter<String, Persona> {

    private final PersonaRepositorio personaRepositorio;

    public PersonaConverter(PersonaRepositorio personaRepositorio) {
        this.personaRepositorio = personaRepositorio;
    }

    @Override
    public Persona convert(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }

        return personaRepositorio.findById(Long.valueOf(id))
                .orElse(null);
    }
}
