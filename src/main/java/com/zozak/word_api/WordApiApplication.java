package com.zozak.	word_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = {"com.zozak.word_api"})
@SpringBootApplication
@EntityScan(basePackages = {"com.zozak.word_api"})
@EnableJpaRepositories(basePackages = {"com.zozak.word_api"})

public class WordApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WordApiApplication.class, args);
	}

}
