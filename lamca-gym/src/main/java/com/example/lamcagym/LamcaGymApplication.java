package com.example.lamcagym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Huvudklassen för Spring Boot-applikationen
@SpringBootApplication
public class LamcaGymApplication {

	// Main-metoden som startar Spring Boot-applikationen
	public static void main(String[] args) {

		// Starta Spring-applikationen genom att köra SpringApplication.run() med den här klassen och eventuella argument
		SpringApplication.run(LamcaGymApplication.class, args);
	}
}