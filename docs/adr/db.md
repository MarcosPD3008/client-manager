# ADR-001: Selección de Base de Datos para Client Manager

## Estado
*Aceptado*

## Contexto
La aplicación requiere una base de datos confiable y escalable para almacenar y gestionar la información de los clientes de manera eficiente. La base de datos seleccionada debe soportar transacciones, indexación e integridad de datos, al mismo tiempo que se integre bien con el framework **Quarkus**. Las principales opciones consideradas fueron **PostgreSQL**, **MySQL** y **MongoDB**.

## Decisión
Se eligió **PostgreSQL** como base de datos del proyecto debido a sus capacidades transaccionales robustas, su fuerte soporte comunitario y su integración fluida con **Quarkus** a través de **Hibernate ORM con Panache**.

## Consecuencias

### ✅ Pros:
- **Cumplimiento ACID:** Garantiza la consistencia e integridad de los datos.
- **Integración Sólida:** Soporte completo con Hibernate ORM y Quarkus.
- **Escalabilidad:** Maneja grandes volúmenes de datos eficientemente mediante indexación y particionamiento.

### ❌ Contras:
- **Mayor Complejidad de Configuración:** Comparado con H2 o SQLite para desarrollo.
- **Uso de Memoria:** Puede requerir más memoria en comparación con MySQL en ciertos escenarios.
- **Curva de Aprendizaje:** Si los miembros del equipo no están familiarizados con las características específicas de PostgreSQL.

## Alternativas Consideradas
1. **MySQL**
   - Alternativa popular para muchas aplicaciones web.
   - Carece de algunas funciones transaccionales avanzadas presentes en PostgreSQL.
   - Menos optimizado para el manejo de JSON.
2. **MongoDB**
   - Base de datos NoSQL con esquema flexible.
   - No es ideal para datos relacionales y consistencia transaccional.
   - Requeriría una adaptación significativa para el manejo de consultas.
3. **H2 (Solo para Desarrollo)**
   - útil para pruebas en memoria, pero no adecuado para producción.
   - No persiste datos después de reiniciar la aplicación.

## Referencias
- [Quarkus Hibernate ORM con Panache](https://quarkus.io/guides/hibernate-orm-panache)
- [Documentación Oficial de PostgreSQL](https://www.postgresql.org/docs/)
- [Fuentes de Datos en Quarkus](https://quarkus.io/guides/datasource)

---
*Creado el: 2025-02-17*

