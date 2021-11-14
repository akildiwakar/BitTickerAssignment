package com.bitoasis.ticker;

import com.bitoasis.ticker.entity.Role;
import com.bitoasis.ticker.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class BitTickerApplication{

	public static void main(String[] args) {
		SpringApplication.run(BitTickerApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	/*
	 * @Autowired private RoleRepository roleRepository;
	 * 
	 * @Override public void run(String... args) throws Exception {
	 * 
	 * if(!roleRepository.existsByName("ROLE_ADMIN")) { Role adminRole = new Role();
	 * adminRole.setName("ROLE_ADMIN"); roleRepository.save(adminRole); }
	 * 
	 * if(!roleRepository.existsByName("ROLE_USER")) { Role userRole = new Role();
	 * userRole.setName("ROLE_USER"); roleRepository.save(userRole); }
	 * 
	 * }
	 */

}
