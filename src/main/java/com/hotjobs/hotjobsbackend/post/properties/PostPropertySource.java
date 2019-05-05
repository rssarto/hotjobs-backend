package com.hotjobs.hotjobsbackend.post.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:post.properties")
public class PostPropertySource {
	
	@Value("#{T(java.lang.Integer).parseInt('${application.config.post.daysToKeep}')}")
	private Integer daysToKeep;

	public int getDaysToKeep() {
		return daysToKeep;
	}
}
