package com.hotjobs.hotjobsbackend.post.domain;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection=Post.COLLECTION_NAME)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post extends AbstractDocument {
	
	public static final String COLLECTION_NAME = "post";
	public static final String STR_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

	public static final DateTimeFormatter CREATION_DATE_FORMATTER = DateTimeFormatter.ofPattern(STR_DATE_FORMAT);
	
	private String text;
	private Date createdAt;
	private List<PostEntity> entities;
	private List<String> relatedLinks;
	
	@Override
	public boolean equals(Object o) {
		if( this == o ) {
			return true;
		}
		
		if( o == null || !(o instanceof Post) ) {
			return false;
		}
		
		Post other = (Post) o;
		final EqualsBuilder equalsBuilder = new EqualsBuilder();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(STR_DATE_FORMAT); 
		equalsBuilder
			.append(this.text, other.text)
			.append(simpleDateFormat.format(this.createdAt), simpleDateFormat.format(other.createdAt));
		
		if( (this.entities != null) ) {
			equalsBuilder
				.append(this.entities.size(), other.entities.size());
				
		}
		
		if( ( this.entities != null && !this.entities.isEmpty() ) && ( other.entities != null && !other.entities.isEmpty() ) &&
			  this.entities.size() == other.entities.size()) {
			
			for( int index = 0; index < this.entities.size(); index++ ) {
				equalsBuilder.append(this.entities.get(index), other.entities.get(index));
			}
		}
		
		if( (this.relatedLinks != null) ) {
			equalsBuilder
				.append(this.relatedLinks.size(), other.relatedLinks.size());
				
		}
		
		if( ( this.relatedLinks != null && !this.relatedLinks.isEmpty() ) && ( other.relatedLinks != null && !other.relatedLinks.isEmpty() ) &&
			  this.relatedLinks.size() == other.relatedLinks.size()) {
			
			for( int index = 0; index < this.relatedLinks.size(); index++ ) {
				equalsBuilder.append(this.relatedLinks.get(index), other.relatedLinks.get(index));
			}
		}
		
		return equalsBuilder.isEquals();
	}
}
