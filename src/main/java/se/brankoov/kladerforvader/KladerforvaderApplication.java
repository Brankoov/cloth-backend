package se.brankoov.kladerforvader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

// src/main/java/.../KladerforvaderApplication.java
@SpringBootApplication
@ConfigurationPropertiesScan   // <-- lägg till detta
public class KladerforvaderApplication {
	public static void main(String[] args) {
		SpringApplication.run(KladerforvaderApplication.class, args);
	}
}
