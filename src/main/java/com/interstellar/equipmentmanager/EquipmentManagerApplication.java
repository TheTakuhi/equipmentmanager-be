package com.interstellar.equipmentmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaRepositories("com.interstellar.equipmentmanager.repository")
@EntityScan(basePackages = "com.interstellar.equipmentmanager.model.entity")
@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
public class EquipmentManagerApplication {
	public static void main(String[] args) {
		SpringApplication.run(EquipmentManagerApplication.class, args);
	}
}


//todo sending emails?
