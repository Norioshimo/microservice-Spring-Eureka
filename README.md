Proyecto Microservicios con Spring Boot y Spring Cloud (EUREKA)
======================================================

Este proyecto implementa una arquitectura de microservicios utilizando Spring Boot 3.4.5, 
Spring Cloud, Java 17 y Apache Maven 3.9.9. La arquitectura incluye servicios registrados 
en Eureka Server, gestionados con Spring Cloud Config, y accedidos mediante un API Gateway.

Tecnologías utilizadas
----------------------
- Java 17
- Spring Boot 3.4.5
- Spring Cloud
- Apache Maven 3.9.9
- Eureka Server (Service Discovery)
- Spring Cloud Config Server (Configuración centralizada)
- Spring Cloud Gateway
- Tomcat (servidor por defecto)
- IDE recomendado: IntelliJ IDEA, Spring Tool Suite o NetBeans

Opcioes para estetica
-------------------

1. Crear el proyecto
   Utiliza https://start.spring.io/ para generar cada uno de los microservicios.

2. Crear un banner personalizado (opcional)
   Puedes generar un banner con arte ASCII en:
   https://devops.datenkollektiv.de/banner.txt/index.html
   Coloca el banner generado en:
   src/main/resources/banner.txt

Validar configuración de Maven
------------------------------
Ejecuta el siguiente comando para verificar la instalación de Maven:

    mvn --version

Cómo ejecutar el proyecto
-------------------------

Ejecutar desde código fuente:

    mvn spring-boot:run

Compilar el proyecto:

    mvn clean package

Ejecutar el JAR compilado:

    java -jar .\\target\\microservice-eureka-0.0.1-SNAPSHOT.jar

Orden de ejecución de los servicios
-----------------------------------

1. config – Servidor de configuración centralizada
2. eureka – Service Discovery
3. gateway – API Gateway
4. Los demás microservicios

Recomendaciones
---------------
- Asegúrate de que todos los microservicios estén configurados para registrarse en Eureka.
- Verifica que los archivos application.yml o bootstrap.yml tengan correctamente definidas 
  las propiedades del servidor de configuración y Eureka.
- Usa perfiles como dev, prod, etc., para manejar distintas configuraciones por entorno.


Soporte
-------
Si encuentras errores o quieres sugerir mejoras, no dudes en abrir un issue o enviar un pull request.
"""
