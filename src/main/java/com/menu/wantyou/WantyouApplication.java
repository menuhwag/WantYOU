package com.menu.wantyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WantyouApplication {

	public static void main(String[] args) {
		SpringApplication.run(WantyouApplication.class, args);
	}

}
