package com.cooper_filme.script_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.cooper_filme.shared_model.repository")
@EntityScan(basePackages = "com.cooper_filme.shared_model.model.entity")
public class ScriptServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScriptServiceApplication.class, args);
	}

}
