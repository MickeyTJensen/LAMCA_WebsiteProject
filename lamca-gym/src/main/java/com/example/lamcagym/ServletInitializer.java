package com.example.lamcagym;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

// Servlet-initialiserare för att konfigurera applikationen för körning som en servlet i en servlet-behållare (t.ex. Tomcat)
public class ServletInitializer extends SpringBootServletInitializer {

	// Överstiger metoden `configure` från SpringBootServletInitializer för att konfigurera applikationen
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		// Returnerar en konfiguration av Spring-applikationen baserat på klassen LamcaGymApplication
		return application.sources(LamcaGymApplication.class);
	}

}