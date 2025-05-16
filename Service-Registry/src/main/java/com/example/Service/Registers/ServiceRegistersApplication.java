package com.example.Service.Registers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistersApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceRegistersApplication.class, args);
	}


}
