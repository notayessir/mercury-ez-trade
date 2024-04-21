package com.notayessir.stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.notayessir.*")
public class StreamApplication {


	public static void main(String[] args) throws Exception {
		SpringApplication.run(StreamApplication.class, args);
	}
}
