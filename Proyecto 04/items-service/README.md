

Como ejemplo se implenta usando 
- FeignClient. Se implementa el ProductFeignClient
- WebClient. Se usa la configuración de WebClientConfig. Se trabaja con ProductServiceWebClient

| Característica                     | RestTemplate / WebClient                 | Feign                                                                      |
| ---------------------------------- | ---------------------------------------- | -------------------------------------------------------------------------- |
| **Estilo de uso**                  | Imperativo / manual                      | Declarativo / basado en interfaces                                         |
| **Definición de endpoints**        | Manual (URL completa)                    | Automática usando nombre de servicio y anotaciones                         |
| **Soporte asincrónico**            | RestTemplate ❌ / WebClient ✔             | Limitado, no reactive por defecto                                          |
| **Manejo de errores**              | Manual                                   | Básico automático (integrado con Spring Cloud)                             |
| **Boilerplate**                    | Alto (mucho código repetitivo)           | Bajo (solo interfaces y anotaciones)                                       |
| **Integración con microservicios** | Manual (URL fija o propiedades)          | Automática con Eureka / Ribbon / Spring Cloud                              |
| **Uso recomendado**                | Llamadas a APIs externas o control total | Comunicación entre microservicios internos con descubrimiento de servicios |


## Ejemplos de uso
### RestTemplate (síncrono):

    RestTemplate restTemplate = new RestTemplate();
    
    Product product = restTemplate.getForObject("http://localhost:8080/products/1", Product.class);

### WebClient (reactivo/asincrónico):
    WebClient client = WebClient.create("http://localhost:8080");
    
    Mono<Product> productMono = client.get()
    .uri("/products/1")
    .retrieve()
    .bodyToMono(Product.class);

### Feign (declarativo con Spring Cloud):
En Interfaces

    @FeignClient(name = "products-service")
    public interface ProductClient {
        @GetMapping("/products/{id}"){
            Product getProduct(@PathVariable("id") Long id);
        }
    }

En una clase

    @Autowired
    private ProductClient productClient;

    Product product = productClient.getProduct(1L);



## Resumen

RestTemplate/WebClient: más control, flexible, más código.

Feign: menos código, más simple para microservicios, se integra con Eureka y load balancing automáticamente.