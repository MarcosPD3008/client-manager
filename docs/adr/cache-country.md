# ADR-003: Cacheo en el Servicio de Países

## Estado
*Aceptado*

## Contexto
El servicio `CountriesService` obtiene la información del gentilicio de un país a partir de un servicio externo (`restcountries api`). Sin embargo, esta información no cambia frecuentemente y depender completamente del servicio externo puede generar problemas de disponibilidad y rendimiento si este falla o responde lentamente.

## Decisión
Se implementa un mecanismo de **cacheo** utilizando `@CacheResult` en Quarkus para almacenar los gentilicios obtenidos y evitar llamadas innecesarias al servicio externo.

```java
@CacheResult(cacheName = "country-demonym-cache")
public String fetchDemonymByCountryCode(String countryCode) {
    // Lógica de obtención de datos
}
```

## Consecuencias

### ✅ Pros:
- **Mejor rendimiento:** Se reducen las llamadas a la API externa, disminuyendo la latencia y mejorando la respuesta del sistema.
- **Resiliencia:** En caso de que el servicio externo falle, el sistema puede seguir funcionando con datos en caché.
- **Reducción de costos:** Menos llamadas a servicios externos pueden reducir costos si estos son pagos o tienen límites de uso.

### ❌ Contras:
- **Datos desactualizados:** Si el servicio externo actualiza la información y la caché no se invalida, se puede mostrar información obsoleta.
- **Manejo de invalidación:** Puede ser necesario establecer estrategias de expiración o actualización de la caché para evitar datos desactualizados por períodos prolongados.

## Alternativas Consideradas
1. **Sin caché** (Rechazada)
   - Dependencia constante del servicio externo.
   - Problemas de disponibilidad y rendimiento en caso de caídas del servicio.

2. **Almacenamiento en Base de Datos** (Rechazada)
   - Requiere una estructura adicional y aumenta la complejidad del mantenimiento.
   - Puede no justificar su uso si la frecuencia de consultas no es elevada.

## Referencias
- [Quarkus Caching Guide](https://quarkus.io/guides/cache)

---
*Creado el: 2025-02-20*

