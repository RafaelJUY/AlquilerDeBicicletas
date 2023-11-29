# Alquiler de Bicicletas

### El presente es un __Trabajo Practico Integrador__ de la materia de __tercer año Backend de Aplicaciones__ de la carrera __Ingeniería en Sistemas de Información de la Universidad Tecnológica Nacional Facultad Regional Córdoba.__

## Organizacion del proyecto
![image](https://github.com/RafaelJUY/AlquilerDeBicicletas/assets/53543640/f97cb33b-856e-4bc5-b3ff-07be6c463557)

## Resumen del proyecto
* Microservicio Alquileres
  * Para gestionar el inicio y fin de los alquileres como así también los clientes.
  * Se comunica con el servicio de estaciones (por ejemplo, para saber en que estación inicio el alquiler y en cual estación se devolvió la bicicleta).
  * Se comunica con un servicio externo para conversión de monedas (para mostrar el costo del alquiler en una moneda especifica).
* Microservicio Estaciones
  * Para gestionar las estaciones existentes.
* Gateway
  * Para dar un único punto de acceso a la aplicación. 
* Persistencia
  * Base de Datos relacional SQLite.
* Keycloak
  * Usado para seguridad de la aplicación.
  * Dos roles: Administrador y Cliente.
* Conversor de moneda
  * Para poder convertir el importe a una moneda seleccionada por el cliente.


## Descripción del Proyecto

Clientes Registrados:
* Solo los clientes registrados pueden alquilar bicicletas.

Alquiler de Bicicletas:
* Cada alquiler implica retirar la bicicleta de una estación y devolverla en otra estación diferente.

Disponibilidad de Bicicletas:
* Se asume que siempre hay una bicicleta disponible en cada estación y espacio disponible para la devolución.

Cálculo de Costos:
* El precio de alquiler se calcula al devolver la bicicleta, considerando un costo fijo, costo por hora, y un cargo adicional por distancia recorrida.
* Descuentos aplicables en días promocionales configurados en el sistema.

Selección de Moneda:
* Los clientes eligen la moneda en la que desean ver el importe adeudado al finalizar el alquiler.

## Funcionalidades del Backend
Funcionalidades mínimas:

* Consultar el listado de estaciones disponibles en la ciudad.
* Consultar los datos de la estación más cercana a una ubicación provista por el cliente.
* Iniciar el alquiler de una bicicleta desde una estación específica.
* Finalizar un alquiler en curso, mostrando el costo en la moneda elegida por el cliente o en pesos argentinos si no se especifica.
* Agregar una nueva estación al sistema.
* Obtener un listado de alquileres realizados con posibilidad de aplicar filtros.
