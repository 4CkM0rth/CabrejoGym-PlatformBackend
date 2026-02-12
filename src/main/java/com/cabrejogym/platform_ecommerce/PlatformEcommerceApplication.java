package com.cabrejogym.platform_ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PlatformEcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlatformEcommerceApplication.class, args);
	}

}
