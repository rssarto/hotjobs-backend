package com.hotjobs.hotjobsbackend.post.domain;

import org.springframework.data.annotation.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class AbstractDocument {
	
	@Id
	private String _id;

}
