package com.rohi.spring_mongo;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

	@Bean
	@ServiceConnection
	MongoDBContainer mongoDbContainer() {
		return new MongoDBContainer(DockerImageName.parse("mongo:latest"));
	}

    //Then, we define a DynamicPropertyRegistrar bean to configure the MongoDB connection URI,
    // allowing our application to connect to the started container.

    // we define the database name against which we want to perform CRUD operations against.
    // If the database with the configured name does not exist, MongoDB will create one for us.
//    @Bean
//    DynamicPropertyRegistrar dynamicPropertyRegistrar(MongoDBContainer mongoDbContainer) {
//        return registry -> {
//            registry.add("spring.data.mongodb.uri", mongoDbContainer::getConnectionString);
//            registry.add("spring.data.mongodb.database", () -> "technical-content-management");
//        };
//    }
}
