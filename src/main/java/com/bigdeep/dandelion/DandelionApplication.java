package com.bigdeep.dandelion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class DandelionApplication {

	public static void main(String[] args) {
		SpringApplication.run(DandelionApplication.class, args);
	}
}
