package com.drz.search;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({
		"com.drz.search.*",
})
@SpringBootApplication
@EnableRabbit
public class SearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchApplication.class, args);
	}

}

