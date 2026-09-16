package com.desi.gestioninmobiliaria.servicio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desi.gestioninmobiliaria.enums.EstadoContrato;
import com.desi.gestioninmobiliaria.enums.EstadoDisponibilidad;
import com.desi.gestioninmobiliaria.enums.TipoPropiedad;
import com.desi.gestioninmobiliaria.modelo.HistorialEstadoPropiedad;
import com.desi.gestioninmobiliaria.modelo.Propiedad;
import com.desi.gestioninmobiliaria.repositorio.ContratoRepositorio;
import com.desi.gestioninmobiliaria.repositorio.HistorialEstadoPropiedadRepositorio;
import com.desi.gestioninmobiliaria.repositorio.PropiedadRepositorio;

@Service
public class PropiedadServicio { //esta es la capa serivio: donde van las reglas 
//Esto significa q servicio necesita 3 repositorios
    private final PropiedadRepositorio propiedadRepositorio;
    private final ContratoRepositorio contratoRepositorio;
    private final HistorialEstadoPropiedadRepositorio historialEstadoPropiedadRepositorio;
//ej: si una propiedad cambia de disponible a alquilada, ese cambio
 //puede quedar registrado en el hisotrial
    public PropiedadServicio(  //cuando spring crea propiedad servicio le pasa los repositorios que necesita usar
            PropiedadRepositorio propiedadRepositorio,
            ContratoRepositorio contratoRepositorio,
            HistorialEstadoPropiedadRepositorio historialEstadoPropiedadRepositorio) {
        this.propiedadRepositorio = propiedadRepositorio;
        this.contratoRepositorio = contratoRepositorio;
        this.historialEstadoPropiedadRepositorio = historialEstadoPropiedadRepositorio;
    }//guarda estos repositorios detro del servicio para poder usarlos despues

    public List<Propiedad> listarNoEliminadas() {
        return propiedadRepositorio.findByEliminadoFalse();
    }  //este metodo devuelve las propiedades no eliminadas
    public List<Propiedad> listarFiltradas(
            String direccion,
            String ciudad,
            TipoPropiedad tipoPropiedad,
            EstadoDisponibilidad estadoDisponibilidad) {
//este metodo recibe los filtros q puso el ususario y devuelve las propiedades q coinciden
        return propiedadRepositorio.findByEliminadoFalse()
                .stream() //permite recorrer esa lista y aplicar filtros
                .filter(propiedad -> direccion == null || direccion.isBlank() //filtra por direccion, si el usuario no escribio nada no aplica filtro, sia escribio algo y pasa las propiedades
                        || propiedad.getDireccion().toLowerCase().contains(direccion.toLowerCase()))
                .filter(propiedad -> ciudad == null || ciudad.isBlank()
                        || propiedad.getCiudad().toLowerCase().contains(ciudad.toLowerCase()))
                .filter(propiedad -> tipoPropiedad == null
                        || propiedad.getTipoPropiedad() == tipoPropiedad)
                .filter(propiedad -> estadoDisponibilidad == null
                        || propiedad.getEstadoDisponibilidad() == estadoDisponibilidad)
                .toList();
    }//este bloque es el filtra las propiedades 

    public Propiedad buscarPorId(Long id) {
        return propiedadRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe la propiedad indicada"));
    } //busca una propiedad por su num id. si existe la tre sino muestra error

    @Transactional
    public Propiedad crear(Propiedad propiedad) {
        validarDuplicado(propiedad);

        if (propiedad.getEstadoDisponibilidad() == null) {
            propiedad.setEstadoDisponibilidad(EstadoDisponibilidad.DISPONIBLE);
        }

        propiedad.setEliminado(false);
        Propiedad propiedadGuardada = propiedadRepositorio.save(propiedad);

        historialEstadoPropiedadRepositorio.save(
                new HistorialEstadoPropiedad(
                        propiedadGuardada,
                        null,
                        propiedadGuardada.getEstadoDisponibilidad()
                )
        );

        return propiedadGuardada;
    } //este metodo valida, guarda la propiedad y registra su estado inicial

    @Transactional
    public Propiedad modificar(Long id, Propiedad datosNuevos) {
        Propiedad propiedadActual = buscarPorId(id);

        Optional<Propiedad> duplicada = propiedadRepositorio
                .findByDireccionIgnoreCaseAndCiudadIgnoreCaseAndEliminadoFalse(
                        datosNuevos.getDireccion(),
                        datosNuevos.getCiudad()
                );

        if (duplicada.isPresent() && !duplicada.get().getId().equals(id)) {
            throw new IllegalArgumentException("Ya existe una propiedad activa con esa dirección y ciudad");
        }

        EstadoDisponibilidad estadoAnterior = propiedadActual.getEstadoDisponibilidad();
        EstadoDisponibilidad estadoNuevo = datosNuevos.getEstadoDisponibilidad();

        boolean tieneContratoActivo = contratoRepositorio
                .findByPropiedadAndEstadoContratoAndEliminadoFalse(propiedadActual, EstadoContrato.ACTIVO)
                .isPresent();

        if (tieneContratoActivo &&
                (estadoNuevo == EstadoDisponibilidad.DISPONIBLE || estadoNuevo == EstadoDisponibilidad.INACTIVA)) {
            throw new IllegalArgumentException(
                    "No se puede cambiar el estado a disponible o inactiva porque la propiedad tiene un contrato activo"
            );
        }

        propiedadActual.setDireccion(datosNuevos.getDireccion());
        propiedadActual.setCiudad(datosNuevos.getCiudad());
        propiedadActual.setTipoPropiedad(datosNuevos.getTipoPropiedad());
        propiedadActual.setCantidadAmbientes(datosNuevos.getCantidadAmbientes());
        propiedadActual.setMetrosCuadrados(datosNuevos.getMetrosCuadrados());
        propiedadActual.setDescripcion(datosNuevos.getDescripcion());
        propiedadActual.setPropietario(datosNuevos.getPropietario());
        propiedadActual.setEstadoDisponibilidad(estadoNuevo);

        Propiedad propiedadGuardada = propiedadRepositorio.save(propiedadActual);

        if (estadoAnterior != estadoNuevo) {
            historialEstadoPropiedadRepositorio.save(
                    new HistorialEstadoPropiedad(propiedadGuardada, estadoAnterior, estadoNuevo)
            );
        }

        return propiedadGuardada;
    }
//este metodo busca la propiedad, valida los cambios q sean correctos, actualiza los datos, guarda en la base 
    //y registra el hisotrial si cambio el estado
    
    @Transactional
    public void eliminar(Long id) {
        Propiedad propiedad = buscarPorId(id);

        boolean tieneContratoActivo = contratoRepositorio
                .findByPropiedadAndEstadoContratoAndEliminadoFalse(propiedad, EstadoContrato.ACTIVO)
                .isPresent();

        if (tieneContratoActivo) {
            throw new IllegalArgumentException("No se puede eliminar una propiedad con contrato activo vigente");
        }

        propiedad.setEliminado(true);
        propiedadRepositorio.save(propiedad);
    }

    private void validarDuplicado(Propiedad propiedad) {
        Optional<Propiedad> existente = propiedadRepositorio
                .findByDireccionIgnoreCaseAndCiudadIgnoreCaseAndEliminadoFalse(
                        propiedad.getDireccion(),
                        propiedad.getCiudad()
                );

        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe una propiedad activa con esa dirección y ciudad");
        }
    }
}//el metodo elimina busca la propiedad por id, controla q no tenga contrato activo 
//si esta todo ok la marca como eliminada sin borrar de la base
//el metodo validarDuplicado controla q no exista otra propiedad con misma direccion y cuidad 