package com.geometrypuzzle.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		Math.atan2(0,1);
		SpringApplication.run(Application.class, args);
	}
}
