package com.ms.config;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
@Slf4j
public class ConfigServiceApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("GIT_TOKEN", dotenv.get("GIT_TOKEN"));
		System.setProperty("GIT_USER", dotenv.get("GIT_USER"));
		System.setProperty("GIT_URI", dotenv.get("GIT_URI"));

		//log.info("Token: "+dotenv.get("GIT_TOKEN"));
		SpringApplication.run(ConfigServiceApplication.class, args);
	}

}
