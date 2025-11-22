package com.msvc.notification;

import com.msvc.notification.event.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotificactionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificactionServiceApplication.class, args);
	}

	@KafkaListener(topics = "notificationTopic")
	public void handleNotification(OrderPlacedEvent orderPlacedEvent){
		log.info("Notificación Recibida: "+orderPlacedEvent.getNumeroPedido());
	}

}
