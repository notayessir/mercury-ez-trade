
package com.notayessir.quote;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@MapperScan("com.notayessir.**.mapper")
@SpringBootApplication
@ComponentScan(basePackages = "com.notayessir.*")
public class QuoteApplication  {



	public static void main(String[] args)  {
		SpringApplication.run(QuoteApplication.class, args);
	}
}
