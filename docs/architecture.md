# Arquitectura del Proyecto

## Introducción
El sistema está diseñado con una arquitectura en capas que separa las responsabilidades en **presentación**, **negocio** y **datos**. Además, se integra con un servicio externo para obtener información adicional sobre países, aprovechando mecanismos de **cacheo** para mejorar el rendimiento.

## Diagrama de Arquitectura
<img src="imgs/client-manager.arch.png" alt="Diagrama de Arquitectura" width="400"/>

## Descripción de las Capas

### 1. Presentación
- **Cliente(s)**: Puede ser una aplicación web o cualquier otro sistema que consuma la API.
- Se comunica con el backend (BE Quarkus) a través de peticiones HTTP.

### 2. Negocio
- **BE Quarkus**: Implementa la lógica del negocio, gestiona clientes y roles, y expone una API REST.
- **Cache**: Se usa para almacenar respuestas de consultas externas y mejorar el rendimiento.
- **Rest Countries**: API externa utilizada para obtener información sobre países (demonym, entre otros).
- La comunicación entre el backend y el servicio externo se realiza mediante peticiones HTTP.

### 3. Datos
- **DB PostgreSQL**: Base de datos relacional que almacena la información de clientes y otros datos del sistema.
- El backend interactúa con la base de datos mediante **Hibernate ORM con Panache** para realizar operaciones de persistencia.

## Flujo de Datos
1. Un cliente envía una petición al backend.
2. **BE Quarkus** maneja la lógica de negocio y consulta la base de datos si es necesario.
3. Si la información requerida está relacionada con un país, el backend consulta la **cache** antes de hacer una petición al servicio externo.
4. Si la información no está en la cache, se realiza una petición a **Rest Countries**.
5. La respuesta del servicio externo se guarda en la cache para futuras consultas.
6. El backend responde al cliente con la información solicitada.
