# ADR-002: Elección del Mecanismo de Paginación

## Estado
*Aceptado*

## Contexto
El sistema necesita un mecanismo eficiente de paginación para gestionar grandes volúmenes de datos y evitar enviar respuestas demasiado grandes al frontend. Si se enviara toda la data en una sola respuesta, podría causar problemas de rendimiento y sobrecarga tanto en el servidor como en el cliente.

Además, la paginación mejora la experiencia del usuario al permitirle navegar por los datos de manera organizada y estructurada.

## Decisión
Se eligió implementar la paginación utilizando **Quarkus con Hibernate ORM Panache**, aprovechando su soporte nativo para la gestión eficiente de consultas paginadas mediante `Page.of(pageNumber - 1, pageSize)`.

El repositorio genérico gestiona la lógica de paginación, evitando la redundancia en la implementación de distintos endpoints.

## Consecuencias

### ✅ Pros:
- **Eficiencia**: Reduce la carga en el backend al limitar la cantidad de datos retornados en cada consulta.
- **Escalabilidad**: Facilita la manipulación de grandes volúmenes de datos sin afectar el rendimiento.
- **Modularidad**: Implementado en el repositorio genérico, permitiendo su reutilización en diferentes entidades.
- **Simplicidad**: Integrado con Hibernate ORM y Quarkus de manera nativa.

### ❌ Contras:
- **Mayor complejidad inicial**: Se requiere definir correctamente los métodos de paginación y validar los parámetros de entrada.
- **Dependencia en Hibernate Panache**: Puede limitar la flexibilidad si se requiere cambiar de ORM en el futuro.

## Alternativas Consideradas
1. **Carga Completa (Sin Paginación)**
   - Más sencilla de implementar.
   - No escalable: grandes volúmenes de datos causarían problemas de rendimiento.
2. **Paginación con Offset y Limit Manual**
   - Requiere definir `OFFSET` y `LIMIT` manualmente en las consultas SQL.
   - Más flexible pero con mayor carga de implementación.
3. **Cursor-Based Pagination**
   - Ideal para bases de datos con estructuras de datos grandes y cambiantes.
   - Más complejo de implementar con Hibernate ORM.

## Referencias
- [Documentación de Paginación en Hibernate Panache](https://quarkus.io/guides/hibernate-orm-panache#pagination)
- [Buenas Prácticas en Paginación](https://developer.mozilla.org/en-US/docs/Web/API/Intersection_Observer_API)

---
*Creado el: 2025-02-20*

