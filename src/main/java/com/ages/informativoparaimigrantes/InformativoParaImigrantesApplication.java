package com.ages.informativoparaimigrantes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.ages.informativoparaimigrantes")
public class InformativoParaImigrantesApplication {
	public static void main(String[] args) {
		SpringApplication.run(InformativoParaImigrantesApplication.class, args);
	}
}

