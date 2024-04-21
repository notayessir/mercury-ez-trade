
package com.notayessir.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = "com.notayessir.*")
@SpringBootApplication
public class GatewayApplication  {



	public static void main(String[] args) throws Exception {
		SpringApplication.run(GatewayApplication.class, args);
	}
}
