
package com.notayessir.queue;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.notayessir.*")
@MapperScan("com.notayessir.**.mapper")
@SpringBootApplication
public class QueueApplication {


	public static void main(String[] args) throws Exception {
		SpringApplication.run(QueueApplication.class, args);
	}
}
