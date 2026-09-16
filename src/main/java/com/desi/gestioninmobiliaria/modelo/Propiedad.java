package com.desi.gestioninmobiliaria.modelo;

import com.desi.gestioninmobiliaria.enums.EstadoDisponibilidad;
import com.desi.gestioninmobiliaria.enums.TipoPropiedad;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity //Propiedad no es solo una clase común: es una entidad que se va a guardar en MySQL.
@Table(name = "propiedades") //La tabla de la base de datos se va a llamar propiedades
public class Propiedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id se genera automaticamente
    private Long id; //id es de tipo long

    @NotBlank(message = "La dirección es obligatoria") //notblack es validaciones: la direccion no puede estar vacia
    private String direccion; //atributo q guarda la direccion de propiedad 

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @NotNull(message = "El tipo de propiedad es obligatorio")//@notnull indica q es obligatorio elegir un tipo
    @Enumerated(EnumType.STRING) // el tipo de propiedad es un enum, una lista de opciones fijas
    private TipoPropiedad tipoPropiedad; //guarda la propiedad elegida

    @NotNull(message = "La cantidad de ambientes es obligatoria")
    @Min(value = 1, message = "La cantidad de ambientes debe ser un número entero positivo")
    private Integer cantidadAmbientes; //guarda la cantidad de ambientes

    @NotNull(message = "Los metros cuadrados son obligatorios")
    @Positive(message = "Los metros cuadrados deben ser un número positivo")
    private Double metrosCuadrados;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "El estado de disponibilidad es obligatorio")
    @Enumerated(EnumType.STRING)
    private EstadoDisponibilidad estadoDisponibilidad = EstadoDisponibilidad.DISPONIBLE;

    @NotNull(message = "El propietario es obligatorio")
    @ManyToOne //muchas propiedades pueden pertenecer a la misma persona
    private Persona propietario;

    private Boolean eliminado = false; //aca se usa para la eliminacion logica
    //false cuando no esta eliminada
    //cuando se elimina cambia a true para ocultar de la lista pero sin borrar de la base de datos

    public Propiedad() { //constructor vacio, sirve para crear una propiedad 
    }

    public Long getId() { //devuelve el id de la propiedad
        return id;
    }

    public void setId(Long id) { //setter asigna un valor a la id de la propiedad
        this.id = id;
    }

    public String getDireccion() { //getter lee la direccion de la propiedad
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    } //setter: sirve para cambiar o cargar direccion de la propiedad

    public String getCiudad() {
        return ciudad;
    } //getter: lee la cuidad de la propiedad

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    } //setter: carga o modifica la cuidad de la propiedad

    public TipoPropiedad getTipoPropiedad() {
        return tipoPropiedad;
    } //getter: lee el tipo de propiedad
    
    //getter y setter siren para acceder a atributos privados de una clase

    public void setTipoPropiedad(TipoPropiedad tipoPropiedad) {
        this.tipoPropiedad = tipoPropiedad;
    } //carga o modifica el tipo de propiedad

    public Integer getCantidadAmbientes() {
        return cantidadAmbientes;
    } //lee la cantidad de ambientes

    public void setCantidadAmbientes(Integer cantidadAmbientes) {
        this.cantidadAmbientes = cantidadAmbientes;
    }

    public Double getMetrosCuadrados() {
        return metrosCuadrados;
    }

    public void setMetrosCuadrados(Double metrosCuadrados) {
        this.metrosCuadrados = metrosCuadrados;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoDisponibilidad getEstadoDisponibilidad() {
        return estadoDisponibilidad;
    }

    public void setEstadoDisponibilidad(EstadoDisponibilidad estadoDisponibilidad) {
        this.estadoDisponibilidad = estadoDisponibilidad;
    }

    public Persona getPropietario() {
        return propietario;
    }

    public void setPropietario(Persona propietario) {
        this.propietario = propietario;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }
}