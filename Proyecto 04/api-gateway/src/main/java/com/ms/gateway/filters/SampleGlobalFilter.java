package com.ms.gateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
@Slf4j
public class SampleGlobalFilter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Ejecutando el filtro antes del request PRE");

        //exchange.getRequest().mutate().headers(h -> h.add("token", "123Token"));
        //log.info("Token anes: "+exchange.getRequest().getHeaders().get("token"));

        ServerHttpRequest mutatedRequest = exchange.getRequest()
                .mutate()
                .headers(h -> h.add("token", "123Token"))
                .build();

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(mutatedRequest)
                .build();


        return chain.filter(mutatedExchange).then(Mono.fromRunnable(() -> {
            log.info("Ejecutando filtro POST response");
            String token = mutatedExchange.getRequest().getHeaders().getFirst("token");
            //if (token != null) {
            //    log.info("Token: " + token);
            //    mutatedExchange.getResponse().getHeaders().add("token", token);
            //}

            Optional.ofNullable(mutatedExchange.getRequest().getHeaders().getFirst("token"))
                    .ifPresent(value -> {
                        log.info("Token Optional: " + value);
                        mutatedExchange.getResponse().getHeaders().add("token", token);
                    });

            mutatedExchange.getResponse().getCookies()
                    .add("color-filtro-global", ResponseCookie.from("color-filtro-global", "red").build());// En la cabecra retornar Cookies
            mutatedExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        }));
    }

    @Override
    public int getOrder() {
        return 100;
    }
}
