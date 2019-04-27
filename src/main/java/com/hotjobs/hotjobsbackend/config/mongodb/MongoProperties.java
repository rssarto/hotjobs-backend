package com.hotjobs.hotjobsbackend.config.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class MongoProperties {

    private final Environment environment;

    @Autowired
    public MongoProperties(Environment environment) {
        this.environment = environment;
    }

    public String getDatabase() {
        return environment.getProperty("spring.data.mongodb.database");
    }

    public String getHost() {
    	return environment.getProperty("spring.data.mongodb.host");
    }
    
    public String getUsername() {
    	return environment.getProperty("spring.data.mongodb.username");    	
    }
    
    public String getPassword() {
    	return environment.getProperty("spring.data.mongodb.password");
    }

    public int getPort() {
        return environment.getProperty("spring.data.mongodb.port", Integer.class);
    }
}
