package com.interstellar.equipmentmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan("com.interstellar.equipmentmanager.model.entity")
@EnableJpaRepositories("com.interstellar.equipmentmanager.*")
@EntityScan("com.interstellar.equipmentmanager.*")
@SpringBootApplication
public class EquipmentManagerApplication {
	public static void main(String[] args) {
		SpringApplication.run(EquipmentManagerApplication.class, args);
	}
}
