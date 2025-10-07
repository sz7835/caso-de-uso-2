package com.delta.deltanet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DeltanetApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeltanetApplication.class, args);
	}

}
