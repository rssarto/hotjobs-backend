package com.hotjobs.hotjobsbackend.post.dao;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Query;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class PostQueryBuilder {
	
	private String text_lower;
	private String entity;
	private Date creationDate;
	private String relatedLink;
	
	public PostQueryBuilder setTextLower(String text) {
		this.text_lower = text;
		return this;
	}
	
	public PostQueryBuilder setEntity(String entity) {
		this.entity = entity;
		return this;
	}
	
	public PostQueryBuilder setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
		return this;
	}
	
	public PostQueryBuilder setRelatedLink(String relatedLink) {
		this.relatedLink = relatedLink;
		return this;
	}
	
	public Query build() {
		final Query query = new Query();
		if( StringUtils.isNotBlank(this.text_lower) ) {
			query.addCriteria(PostDAO.criteriaByLowerText(this.text_lower));
		}
		
		if( StringUtils.isNotBlank(this.entity) ) {
			query.addCriteria(PostDAO.criteriaByEntity(entity));
		}
		
		if( this.creationDate != null ) {
			query.addCriteria(PostDAO.criteriaByCreationDate(this.creationDate));
		}
		
		if( StringUtils.isNotBlank(this.relatedLink) ) {
			query.addCriteria(PostDAO.relatedLinkCriteria(relatedLink));
		}
		return query;
	}
	
}
