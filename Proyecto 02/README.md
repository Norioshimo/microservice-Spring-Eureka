PROYECTO: Microservicios con Spring Boot

Descripción
---------
Proyecto de ejemplo de arquitectura microservicios usando Spring Boot. Incluye servicios para descubrimiento, gateway, servicios de dominio (producto, order, inventario, notificacion), mensajería con Kafka, trazabilidad con Zipkin y autenticación con Keycloak (OAuth2). Bases de datos: MySQL y MongoDB.

Servicios incluidos
------------------
- discovery-server (Eureka)
- api-gateway (Spring Cloud Gateway)
- producto-service
- order-service
- inventario-service
- notificacion-service
- kafka (zookeeper + broker)
- trazabilidad (Zipkin)
- oauth2 (Keycloak)
- prometheus + grafana
- bases de datos: MySQL, MongoDB

Requisitos previos
------------------
- Docker y Docker Compose
- Java 17+ (o la versión que uses en los microservicios)
- Maven
- Kafka client (opcional para pruebas)

Levantar Keycloak (OAuth2)
--------------------------
Para levantar Keycloak en modo developer y mapear el puerto local 8081 -> 8080 en el contenedor (Ejecutar docker composer):

```bash
docker run -p 127.0.0.1:8081:8080 \
  -e KC_BOOTSTRAP_ADMIN_USERNAME=admin \
  -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin \
  quay.io/keycloak/keycloak:26.4.0 start-dev
```

Acceder a la consola de administración:
http://localhost:8081/admin/master/console/

Pasos básicos en Keycloak:
1. Crear (si no existe) un Realm — p. ej. `mi-realm`.
2. Ir a **Clients** y crear un Client para cada servicio que necesite autenticar (o uno central para machine-to-machine).
3. Configurar el Client con `Access Type = confidential`.
4. Tomar **Client ID** y **Client Secret** (Credentials) para configurar en los microservicios.

Configuración recomendada para usar en los servicios (Client Credentials):
- Token Name: definir un nombre descriptive (p. ej. `svc-token`)
- Grant type: Client Credentials
- Access Token URL: (p. ej. http://localhost:8081/realms/spring-boot-microservices-realm/protocol/openid-connect/token)
- Client ID: el nombre del client creado en Keycloak (p. ej. spring-cloud-client)
- Client Secret: valor de credentials
- scope: `openid offline_access`
- Client Authentication: Send as Basic Auth Header

Levantar Zipkin
---------------
Zipkin para trazabilidad (por defecto puerto 9411) (Ejecutar docker composer):

```bash
docker run -d -p 9411:9411 openzipkin/zipkin
```

Arquitectura Docker Compose (ejemplo resumido)
---------------------------------------------
El `docker-compose.yml` del proyecto debe levantar:
- mysql
- mongodb
- zookeeper
- kafka
- keycloak (opcional: ya puedes usar `docker run` separado)
- zipkin (opcional: ya puedes usar `docker run` separado)
- prometheus
- grafana

Ejecutar el compose:

```bash
docker-compose up -d
```

Ejemplo de servicios Spring (resumen de ejecución)
-------------------------------------------------
1. Levantar discovery-server (Eureka)
2. Levantar api-gateway
3. Levantar producto-service
4. Levantar order-service
5. Levantar inventario-service

Cada servicio tiene en `application.yml` las siguientes configuraciones clave:
- conexión a Eureka (discovery)
- nombre del servicio (`spring.application.name`)
- configuración de datasource (MySQL o MongoDB según corresponda)
- configuración de Kafka producers/consumers
- configuración de tracing (Zipkin)
- configuración de OAuth2 (resource-server + client credentials si aplica)


Nota: adapta las URLs y los valores a tu realm y clients.

Configuración de Gateway para propagar token
--------------------------------------------
Configura `spring-cloud-gateway` para pasar el header `Authorization` hacia los microservicios y, si necesitas, validar el token en el gateway.

Kafka
-----
Levantar zookeeper y kafka (en docker-compose). Configura topics para comunicación entre servicios (p. ej. topic `orders`, `inventory-events`).

Bases de datos
--------------
- MySQL: para datos relacionales (orders, productos si prefieres relacional).
- MongoDB: para datos no relacionales (logs, documentos, historiales).

Prometheus + Grafana
--------------------
Añade endpoints Actuator en cada servicio y configura Prometheus para scrapear los endpoints. Grafana puede usarse para dashboards.

Zipkin
------
Configura `spring.zipkin.base-url: http://localhost:9411/` en los microservicios para reportar trazas.

Ejecución local (resumen rápido)
--------------------------------
1. `docker run` o `docker-compose up -d` para infra (MySQL, Mongo, Kafka, Zipkin, Keycloak).
2. Levantar discovery-server.
3. Levantar api-gateway.
4. Levantar servicios (producto, order, inventario, notificacion).
5. Ver trazas en http://localhost:9411
6. Ver Keycloak admin en http://localhost:8081/admin
