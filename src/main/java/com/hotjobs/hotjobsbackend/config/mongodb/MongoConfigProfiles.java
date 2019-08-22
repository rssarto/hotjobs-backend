package com.hotjobs.hotjobsbackend.config.mongodb;

import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Configuration
@Profile("!test") //or else problems when using @EnableAutoConfiguration in Integration ests
public class MongoConfigProfiles {

    private final MongoProperties mongoProperties;

    public MongoConfigProfiles(MongoProperties mongoProperties) {
        this.mongoProperties = mongoProperties;
    }

    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
    	MongoClient mongoClient = null;
    	if( Objects.isNull(mongoProperties.connectionUrl()) ) {
    		mongoClient = new MongoClient(
    				new MongoClientURI("mongodb://" + mongoProperties.getUsername() + ":" + mongoProperties.getPassword() + "@" + mongoProperties.getHost() + ":" + mongoProperties.getPort() + "/post")
    				);
    	}else {
    		mongoClient = new MongoClient(new MongoClientURI(mongoProperties.connectionUrl()));
    	}
        return new SimpleMongoDbFactory(mongoClient, mongoProperties.getDatabase());
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        return mongoTemplate;
    }
}
