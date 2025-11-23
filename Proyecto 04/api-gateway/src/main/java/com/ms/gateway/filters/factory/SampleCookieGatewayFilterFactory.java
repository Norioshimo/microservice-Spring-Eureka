package com.ms.gateway.filters.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/*Filtro personalizado*/
@Component
@Slf4j
public class SampleCookieGatewayFilterFactory extends AbstractGatewayFilterFactory<SampleCookieGatewayFilterFactory.ConfigurationCookie> {

    public SampleCookieGatewayFilterFactory() {
        super(ConfigurationCookie.class);
    }

    @Override
    public GatewayFilter apply(ConfigurationCookie config) {
        log.info("Ejecutando pre gateway filter factory: " + config.message);
        return new OrderedGatewayFilter((exchange, chain) -> {
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {

                Optional.ofNullable(config.value).ifPresent(cookie -> {
                    exchange.getResponse().addCookie(
                            ResponseCookie.from(config.name, cookie).build()
                    );
                });
                log.info("Ejecutando post gateway filter factory: " + config.message);
            }));
        }, 100);
    }

    //@Override
    //public List<String>shortcutFieldOrder(){
    //     return Arrays.asList("message","name","value");
    //}


    public static class ConfigurationCookie {
        private String name;
        private String value;
        private String message;

        public void setName(String name) {
            this.name = name;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public String getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }
}
