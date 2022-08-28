package com.example.websqlnotebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.example.websqlnotebook")
@EnableJpaRepositories("com.example.websqlnotebook.repository")
public class WebsqlnotebookApplication {

	public static void main(String[] args) {
		//
		SpringApplication.run(WebsqlnotebookApplication.class, args);
	}

}
