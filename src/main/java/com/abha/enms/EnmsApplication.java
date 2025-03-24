package com.abha.enms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EnmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnmsApplication.class, args);
	}

}
