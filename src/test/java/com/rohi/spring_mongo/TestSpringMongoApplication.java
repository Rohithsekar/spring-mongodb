package com.rohi.spring_mongo;

import org.springframework.boot.SpringApplication;

public class TestSpringMongoApplication {

	public static void main(String[] args) {
		SpringApplication.from(SpringMongoApplication::main).with(TestcontainersConfiguration.class).run(args);
	}



}
