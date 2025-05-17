package com.blps.lab4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableMethodSecurity
public class GooglePlayApp {
	public static void main(String[] args) {
		SpringApplication.run(GooglePlayApp.class, args);
	}
}

