package com.hotjobs.hotjobsbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class HotjobsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotjobsBackendApplication.class, args);
	}
	

}
