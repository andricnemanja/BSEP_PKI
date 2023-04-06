package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class PkiBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PkiBackendApplication.class, args);
//		Dotenv dotenv = null;
//		dotenv = Dotenv.configure().directory("./").load();
//		System.out.println(dotenv.get("KEY_STORE_PASSWORD"));
	}

}
