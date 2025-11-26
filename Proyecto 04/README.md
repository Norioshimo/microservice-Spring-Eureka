
### Items
- WebClient
- Feign
- Resilience4j(Cicuit Breaker)


Configuración de repositorio en https://github.com/Norioshimo/microservicios-config.git


Se configura el actuator para las metricas
- http://localhost:8005/actuator/health     : Estado de salud de la app (UP/DOWN)
- http://localhost:8005/actuator/metrics    : Métricas de la app (jvm.memory, http.server.requests, etc.)
- http://localhost:8005/actuator/env        : Muestra todas las propiedades y variables de entorno cargadas
- http://localhost:8005/actuator/beans      : Lista todos los beans del contexto de Spring
- http://localhost:8005/actuator/info       : Información general de la aplicación (versión, nombre, etc.)
- http://localhost:8005/actuator/loggers    : Permite ver y cambiar el nivel de logging en tiempo real



### Configurar Environment.
Renombrar el archivo .env.template a .env y completar los valores.
- config-service


