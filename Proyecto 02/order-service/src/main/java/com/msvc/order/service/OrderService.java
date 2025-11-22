package com.msvc.order.service;

import com.msvc.order.dto.InventarioResponse;
import com.msvc.order.dto.OrderLineItemsDto;
import com.msvc.order.dto.OrderRequest;
import com.msvc.order.kafka.OrderProducer;
import com.msvc.order.model.Order;
import com.msvc.order.model.OrderLineItems;
import com.msvc.order.repository.OrderRepository;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderProducer orderProducer;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private Tracer tracer;

    @Transactional
    public String placeOrder(OrderRequest orderRequest) {
        System.out.println("PlaceOrder");
        Order order = new Order();
        order.setNumeroPedido(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineDtoItems()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        order.setOrderLineItems(orderLineItems);

        List<String> codigoSku = order.getOrderLineItems().stream()
                .map(OrderLineItems::getCodigoSku)
                .collect(Collectors.toList());

        System.out.println("Codigos Sku: " + codigoSku);

        Span inventarioServiceLookup = tracer.nextSpan().name("InventarioServiceLookup");

        try (Tracer.SpanInScope isLookUp = tracer.withSpan(inventarioServiceLookup.start())) {
            inventarioServiceLookup.tag("call", "inventario-service");
            InventarioResponse[] inventarioResponses = webClientBuilder.build().get()
                    .uri("http://inventario-service/api/inventario", uriBuilder -> uriBuilder.queryParam("codigoSku", codigoSku).build())
                    .retrieve()
                    .bodyToMono(InventarioResponse[].class)
                    .block();
            System.out.println("inventario Response");
            System.out.println("Cantidad: " + inventarioResponses.length);
            boolean allProductosInStock = Arrays.stream(inventarioResponses).allMatch(InventarioResponse::isInStock);

            if (allProductosInStock) {
                System.out.println("Todos los productos en stock");

                if (order.getOrderLineItems() != null) {
                    order.getOrderLineItems().forEach(item -> item.setOrderid(order));
                }

                // Enviar mensaje al broker de kafka
                orderProducer.sendOrderPlacedEvent(order);
                orderRepository.save(order);
                return "Pedido ordenado con exito";
            } else {
                throw new IllegalArgumentException("El producto no esta en stock");
            }
        } finally {
            inventarioServiceLookup.end();
        }


    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrecio(orderLineItemsDto.getPrecio());
        orderLineItems.setCantidad(orderLineItemsDto.getCantidad());
        orderLineItems.setCodigoSku(orderLineItemsDto.getCodigoSku());
        return orderLineItems;
    }
}
