package com.msvc.order.kafka;

import com.msvc.order.event.OrderPlacedEvent;
import com.msvc.order.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderProducer {

    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public void sendOrderPlacedEvent(Order order) {
        kafkaTemplate.send("notificationTopic",new OrderPlacedEvent(order.getNumeroPedido()));


        log.info("📤 Evento enviado a Kafka: " + order.getNumeroPedido());
    }
}
