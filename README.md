# Gestión Inmobiliaria

Aplicación web para la gestión de propiedades, publicaciones y contratos inmobiliarios, desarrollada con Java y Spring Boot.

El sistema permite administrar personas y propiedades, gestionar publicaciones de inmuebles y registrar contratos, manteniendo además un historial de los cambios de estado realizados durante el proceso.

## Funcionalidades

- Gestión de personas.
- Gestión de propiedades.
- Creación y administración de publicaciones inmobiliarias.
- Gestión de contratos.
- Búsqueda y filtrado de información.
- Seguimiento del historial de estados de propiedades.
- Seguimiento del historial de estados de publicaciones.
- Seguimiento del historial de estados de contratos.
- Persistencia de datos en MySQL.
- Interfaz web mediante Thymeleaf.

## Tecnologías utilizadas

- Java 17
- Spring Boot 4
- Spring MVC
- Spring Data JPA
- Hibernate
- Thymeleaf
- MySQL
- Maven
- HTML
- CSS

## Arquitectura

La aplicación está organizada mediante una arquitectura por capas que separa las diferentes responsabilidades del sistema:

- **Controladores:** reciben las solicitudes HTTP y gestionan la navegación entre las vistas.
- **Servicios:** contienen la lógica de negocio de la aplicación.
- **Repositorios:** gestionan el acceso y la persistencia de los datos mediante Spring Data JPA.
- **Modelo:** contiene las entidades que representan el dominio de la aplicación.
- **Templates:** contienen las vistas dinámicas desarrolladas con Thymeleaf.

## Base de datos

La aplicación utiliza MySQL para la persistencia de datos.

El proyecto trabaja con una base de datos denominada:

```text
gestion_inmobiliaria
```

El repositorio incluye el archivo `script_inicial.sql` para facilitar la inicialización de la base de datos.

## Requisitos

Para ejecutar el proyecto se necesita:

- Java 17 o superior.
- MySQL 8.
- Maven, o utilizar Maven Wrapper incluido en el proyecto.

## Ejecución

1. Clonar el repositorio.

2. Acceder a la carpeta del proyecto.

3. Configurar la conexión a MySQL en `application.properties`.

4. Inicializar la base de datos utilizando `script_inicial.sql` si es necesario.

5. Ejecutar la aplicación.

En Windows:

```bash
mvnw.cmd spring-boot:run
```

En Linux o macOS:

```bash
./mvnw spring-boot:run
```

6. Abrir en el navegador:

```text
http://localhost:8080
```

## Estructura principal

```text
src/main/java/com/desi/gestioninmobiliaria
├── config
├── controlador
├── enums
├── modelo
├── repositorio
└── servicio
```

Esta estructura permite mantener separadas la configuración, la lógica de negocio, el acceso a datos y la capa web de la aplicación.

## Estado del proyecto

El proyecto se encuentra funcional y permite gestionar las principales operaciones relacionadas con propiedades, publicaciones, personas y contratos inmobiliarios.

## Autor

Carla Fernandez