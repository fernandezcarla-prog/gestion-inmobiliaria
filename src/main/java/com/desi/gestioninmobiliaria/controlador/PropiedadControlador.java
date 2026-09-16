package com.desi.gestioninmobiliaria.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.desi.gestioninmobiliaria.enums.EstadoDisponibilidad;
import com.desi.gestioninmobiliaria.enums.TipoPropiedad;
import com.desi.gestioninmobiliaria.modelo.Propiedad;
import com.desi.gestioninmobiliaria.servicio.PersonaServicio;
import com.desi.gestioninmobiliaria.servicio.PropiedadServicio;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/propiedades")
public class PropiedadControlador {

    private final PropiedadServicio propiedadServicio;
    private final PersonaServicio personaServicio;

    public PropiedadControlador(PropiedadServicio propiedadServicio, PersonaServicio personaServicio) {
        this.propiedadServicio = propiedadServicio;
        this.personaServicio = personaServicio;
    }

    @GetMapping
    public String listar(
            @RequestParam(required = false) String direccion,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) TipoPropiedad tipoPropiedad,
            @RequestParam(required = false) EstadoDisponibilidad estadoDisponibilidad,
            Model model) {    // este metodo sirve para mostrar las lista de propiedades, si el usuario pone filtro muestra las propiedades filtradas, si no pone muestra todas las propiedades

        model.addAttribute("propiedades", propiedadServicio.listarFiltradas(
                direccion,
                ciudad,
                tipoPropiedad,
                estadoDisponibilidad)); //llama al servicio para q busque las propiedades, usando los filtros q llegaron

        model.addAttribute("tiposPropiedad", TipoPropiedad.values());
        model.addAttribute("estadosDisponibilidad", EstadoDisponibilidad.values());
// esto carga las opciones posibles de filtros
        model.addAttribute("direccion", direccion);
        model.addAttribute("ciudad", ciudad);
        model.addAttribute("tipoPropiedad", tipoPropiedad);
        model.addAttribute("estadoDisponibilidad", estadoDisponibilidad);
//aca se guarda los valores q el usuario puso en los filtros
        return "propiedades/listado"; //se va a mostrar en pantalla el listado de propiedades
    }

    @GetMapping("/nueva") //metodo q se ejecuta cuando el usuario entra a nueva propiedad
    public String nueva(Model model) {
        model.addAttribute("propiedad", new Propiedad());//crea una propiedad vacia 
        cargarDatosFormulario(model); //carga opciones: tipo, estado, personas para elegir propietario
        return "propiedades/formulario"; // muestra la pantalla del formulario de propiedades
    }

    @PostMapping("/guardar") // ACA EL CONTROLADOR RECIBE LA PROPIEDAD CARGADA EN EL FORMULARIO, PRIMERO LO VALIDA SI ESTA BIEN DESPUES LA MANDA AL SERVICIO PARA FUARDARLA
    public String guardar(
            @Valid Propiedad propiedad,//recibe la propiedad q el usuario completo y valida los datos
            BindingResult bindingResult, //guarda los errores de validacion si los hay
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            cargarDatosFormulario(model); //vuelve a cargar las opciones del formulario
            return "propiedades/formulario"; //vuelve a mostrar el formulario para q corrija el error
        }

        try {
            propiedadServicio.crear(propiedad);
            redirectAttributes.addFlashAttribute("mensaje", "Propiedad creada correctamente");
            return "redirect:/propiedades"; //vuelve al listado de propiedades
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage()); //manda el mensaje de error en la pantalla
            cargarDatosFormulario(model);
            return "propiedades/formulario";
        }
    }

    @GetMapping("/editar/{id}") //metodo que se ejecuta cuando el ususario toca editar
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("propiedad", propiedadServicio.buscarPorId(id)); //busca la propiedad por su id y la manda al formulario
            cargarDatosFormulario(model); //carga la lista de opciones  ej: tipo de propiedad, estado, personas para elegir propietario
            return "propiedades/formulario";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/propiedades";
        } //Este método abre el formulario de edición. Busca la propiedad por ID y muestra sus datos para poder modificarlos.”
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(
            @PathVariable Long id, // toma el id de la propiedad desde la url
            @Valid Propiedad propiedad, //recibe los datos q el usuario cargo en el formulario y los valida
            BindingResult bindingResult, // guarda los errores de valicion si existen
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            propiedad.setEliminado(false);
            cargarDatosFormulario(model);
            return "propiedades/formulario"; //  Si los datos del formulario tienen errores de validación, no actualizo la propiedad. Vuelvo a cargar las opciones del formulario y muestro otra vez el formulario para corregir.
         }

        try {
            propiedadServicio.modificar(id, propiedad);
            redirectAttributes.addFlashAttribute("mensaje", "Propiedad modificada correctamente");
            return "redirect:/propiedades";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            cargarDatosFormulario(model);
            return "propiedades/formulario";
        }  //modifica la propiedad q tiene ese id con los datos nuevos que vienen del formulario
    }

    @GetMapping("/eliminar/{id}") //metodo se usa cuadno el usuario toca eliminar 
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            propiedadServicio.eliminar(id); // el controlador no elimina de manera directa, le pasa el id q se intenta eliminar al servicio
            redirectAttributes.addFlashAttribute("mensaje", "Propiedad eliminada correctamente");
        } catch (IllegalArgumentException e) {  //si no se puede eliminar muestra un mensaje de error
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/propiedades"; //despues de intentar eliminar vuelve al listado de propiedades
    }

    private void cargarDatosFormulario(Model model) { // este metodo prepara las listas desplegable del formulario: tipo, estada y propietario
        model.addAttribute("tiposPropiedad", TipoPropiedad.values());
        model.addAttribute("estadosDisponibilidad", EstadoDisponibilidad.values());
        model.addAttribute("personas", personaServicio.listarNoEliminadas());
    }
}