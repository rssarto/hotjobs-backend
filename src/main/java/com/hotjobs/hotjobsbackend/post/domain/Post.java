package com.hotjobs.hotjobsbackend.post.domain;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection=Post.COLLECTION_NAME)
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Post extends AbstractDocument {
	
	public static final String COLLECTION_NAME = "post";
	public static final String STR_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

	public static final DateTimeFormatter CREATION_DATE_FORMATTER = DateTimeFormatter.ofPattern(STR_DATE_FORMAT);
	
	@NotBlank(message=Messages.Fields.TEXT_MANDATORY)
	private String text;
	private String text_lower;
	private Date createdAt;
	private List<PostEntity> entities;
	private List<String> relatedLinks;
	
	public Post(String text, Date createdAt, List<PostEntity> entities, List<String> relatedLinks) {
		super();
		this.text = text;
		this.text_lower = text.toLowerCase();
		this.createdAt = createdAt;
		this.entities = entities;
		this.relatedLinks = relatedLinks;
	}
	
	@Override
	public boolean equals(Object o) {
		if( this == o ) {
			return true;
		}
		
		if( Objects.isNull(o) || !(o instanceof Post) ) {
			return false;
		}
		
		Post other = (Post) o;
		final EqualsBuilder equalsBuilder = new EqualsBuilder();
		
		if( Objects.nonNull(this.createdAt) && Objects.nonNull(other.createdAt) ) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(STR_DATE_FORMAT); 
			equalsBuilder
			.append(this.text, other.text)
			.append(simpleDateFormat.format(this.createdAt), simpleDateFormat.format(other.createdAt));
		}
		
		if( (Objects.nonNull(this.entities)) ) {
			equalsBuilder
				.append(this.entities.size(), other.entities.size());
				
		}
		
		if( ( Objects.nonNull(this.entities) && !this.entities.isEmpty() ) && ( Objects.nonNull(other.entities) && !other.entities.isEmpty() ) &&
			  this.entities.size() == other.entities.size()) {
			
			for( int index = 0; index < this.entities.size(); index++ ) {
				equalsBuilder.append(this.entities.get(index), other.entities.get(index));
			}
		}
		
		if( (Objects.nonNull(this.relatedLinks)) ) {
			equalsBuilder
				.append(this.relatedLinks.size(), other.relatedLinks.size());
				
		}
		
		if( ( Objects.nonNull(this.relatedLinks) && !this.relatedLinks.isEmpty() ) && ( Objects.nonNull(other.relatedLinks) && !other.relatedLinks.isEmpty() ) &&
			  this.relatedLinks.size() == other.relatedLinks.size()) {
			
			for( int index = 0; index < this.relatedLinks.size(); index++ ) {
				equalsBuilder.append(this.relatedLinks.get(index), other.relatedLinks.get(index));
			}
		}
		
		return equalsBuilder.isEquals();
	}
	
	public static class Messages {
		public static class Fields {
			public static final String TEXT_MANDATORY = "Text is mandatory";
		}
	}
}
