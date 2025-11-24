package com.ms.items.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class AppConfig {

    /*
    Usar por codigo. Es una alternativa de configurar en el application.yml
    *   Propósito
        Este Circuit Breaker:
        Evita sobrecargar un sistema que está fallando
        Reduce latencia para el usuario mientras el servicio está caído
        Permite recuperación automática cuando el servicio vuelve
    **/
    @Bean
    Customizer<Resilience4JCircuitBreakerFactory> cutomizerCircuitBreaker() {


        return (factory) -> factory.configureDefault(id -> {
            return new Resilience4JConfigBuilder(id)
                    .circuitBreakerConfig(
                            CircuitBreakerConfig.custom()
                                    /*  El circuito mira las últimas 10 llamadas para evaluar si el servicio está funcionando bien.
                                        Esa “ventana” puede ser por cantidad de llamadas o por tiempo (dependiendo de la configuración por defecto del sistema)*/
                                    .slidingWindowSize(10)
                                    /*  Si el 50% o más de las últimas llamadas fallaron
                                        El circuito abre (Open).
                                        Ejemplo:
                                        De 10 llamadas en la ventana:
                                        Si 5 fallan → circuito abierto*/
                                    .failureRateThreshold(50)
                                    /*  Cuando el circuito está abierto, no se hacen llamadas al servicio real.
                                        Se espera 10 segundos antes de pasar a estado half-open (medio abierto).*/
                                    .waitDurationInOpenState(Duration.ofSeconds(10L))
                                    /*
                                        Cuando el Circuit Breaker entra en Half-Open, permite solo 5 llamadas hacia el servicio real para probarlo.
                                        Si suficientes llamadas funcionan correctamente → vuelve a Closed (normal).
                                        Si ocurre un número de fallos que supere el umbral → vuelve a Open y bloquea nuevamente.
                                        Controla cuántas llamadas de prueba se permiten cuando el circuito está intentando recuperarse.*/
                                    .permittedNumberOfCallsInHalfOpenState(5)
                                    .slowCallDurationThreshold(Duration.ofSeconds(2))
                                    .slowCallRateThreshold(50)
                                    .build())
                    .timeLimiterConfig(TimeLimiterConfig
                            .custom()
                            .timeoutDuration(Duration.ofSeconds(4L))
                            .build())
                    .build();
        });
    }
}
