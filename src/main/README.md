# Practica de Laboratorio: Arquitectura por Capas con Spring Boot y MySQL

## Descripcion General

Este repositorio contiene la implementacion de una aplicacion web RESTful desarrollada con Spring Boot, cuyo proposito es integrar los principales componentes del ecosistema del framework: las capas Controller, Service, Repository y Domain, junto con el uso de Inversion de Control (IoC), manejo de excepciones personalizado, interceptores HTTP y persistencia de datos en MySQL mediante JPA/Hibernate.

## Objetivos

Configurar un proyecto Spring Boot completo integrando dependencias para desarrollo web, persistencia con JPA, DevTools y conexion a MySQL. Aplicar el principio de Inversion de Control mediante la inyeccion de componentes anotados con `@Controller`, `@Service` y `@Repository`. Implementar un flujo RESTful funcional sobre una entidad de negocio con intercambio de datos a traves de DTOs. Manejar errores y operaciones transversales mediante excepciones personalizadas, un controlador global de errores con `@ControllerAdvice` e interceptores de registro de solicitudes.

## Estructura del Proyecto

```
src/main/java/
    domain/          Entidad JPA que mapea la tabla students
    dto/             DTOs de entrada (validacion) y salida (exposicion)
    repository/      Interfaz JpaRepository con consultas derivadas
    service/         Interfaz de servicio e implementacion de logica de negocio
    controller/      Controlador REST que orquesta las capas
    exception/       Excepciones de dominio y handler global (ControllerAdvice)
    config/          Registro del interceptor HTTP
    interceptor/     Interceptor para logging y medicion de tiempos
src/main/resources/
    application.yml  Configuracion de datasource, JPA, Jackson, DevTools y servidor
```

## Descripcion por Componente

### Entidad de Dominio (`Student`)

Clase JPA que mapea la tabla `students` en MySQL. Define los atributos: clave primaria con autoincremento, nombre obligatorio con longitud maxima de 120 caracteres, email obligatorio y unico a nivel de base de datos, fecha de nacimiento opcional y estado logico con valor por defecto `true`.

### DTOs

Se define un DTO de entrada (`StudentRequestDTO`) que aplica validaciones con anotaciones de Bean Validation antes de que los datos lleguen a la capa de servicio: nombre con entre 3 y 120 caracteres, email con formato valido y fecha de nacimiento opcional. Se define un DTO de salida (`StudentResponseDTO`) que expone unicamente los campos necesarios para el cliente, desacoplando la API del modelo interno.

### Repositorio (`StudentRepository`)

Interfaz que extiende `JpaRepository`, proveyendo operaciones CRUD completas. Incluye metodos derivados para busqueda por email y verificacion de existencia por email.

### Servicio (`StudentService` / `StudentServiceImpl`)

La interfaz declara los contratos de negocio: crear estudiante, obtener por ID, listar todos y desactivar logicamente. La implementacion aplica reglas como la prevencion de emails duplicados, las transiciones de estado validas y el mapeo interno de entidad a DTO de salida. La inyeccion de dependencias se realiza por constructor.

### Controlador REST (`StudentController`)

Expone los siguientes endpoints bajo el prefijo `/api/students`:

| Metodo | Ruta               | Descripcion                        | Codigo de respuesta |
|--------|--------------------|------------------------------------|---------------------|
| POST   | `/`                | Crear un estudiante                | 201 Created         |
| GET    | `/`                | Listar todos los estudiantes       | 200 OK              |
| GET    | `/{id}`            | Obtener estudiante por ID          | 200 OK / 404        |
| PATCH  | `/{id}/deactivate` | Desactivar logicamente             | 200 OK              |

### Manejo de Excepciones (`GlobalExceptionHandler`)

Controlador global anotado con `@ControllerAdvice` que gestiona: `NotFoundException` con respuesta 404, excepciones de conflicto de negocio con respuesta 409, errores de validacion de `@Valid` con respuesta 400 y excepciones genericas no controladas con respuesta 500. Todas las respuestas de error siguen una estructura JSON estandarizada.

### Interceptor HTTP (`LoggingInterceptor`)

Registra el tiempo de inicio y el endpoint invocado en `preHandle`, y calcula la duracion total junto con el codigo de estado HTTP en `afterCompletion`. Se encuentra registrado para todas las rutas bajo `/api/**`.

## Requisitos del Entorno

- JDK 17 o superior
- IntelliJ IDEA Ultimate
- MySQL 8 o superior (instalacion local o contenedor Docker)
- Postman o cliente HTTP equivalente para pruebas

## Configuracion de la Base de Datos

Antes de ejecutar la aplicacion, crear la base de datos en MySQL:

```sql
CREATE DATABASE springlab;
```

Ajustar las credenciales de conexion en `src/main/resources/application.yml` segun el entorno local.

## Ejecucion

Desde IntelliJ IDEA: `Run > SpringLabApplication`. La aplicacion levanta en `http://localhost:8080` con Tomcat embebido. Hibernate ejecuta automaticamente el esquema sobre la base de datos configurada.

## Conceptos Aplicados

Arquitectura por capas (Controller, Service, Repository, Domain), Inversion de Control e inyeccion de dependencias, mapeo objeto-relacional con JPA/Hibernate, transferencia de datos mediante DTOs, validacion declarativa con Bean Validation, manejo centralizado de errores con `@ControllerAdvice`, interceptores HTTP para observabilidad y diseno de APIs RESTful.
"# Ecosistema-Spring" 
