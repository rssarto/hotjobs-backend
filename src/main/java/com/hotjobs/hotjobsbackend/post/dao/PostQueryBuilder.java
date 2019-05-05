package com.hotjobs.hotjobsbackend.post.dao;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
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
			query.addCriteria(criteriaByLowerText(this.text_lower));
		}
		
		if( StringUtils.isNotBlank(this.entity) ) {
			query.addCriteria(criteriaByEntity(entity));
		}
		
		if( this.creationDate != null ) {
			query.addCriteria(criteriaByCreationDate(this.creationDate));
		}
		
		if( StringUtils.isNotBlank(this.relatedLink) ) {
			query.addCriteria(criteriaByRelatedLink(relatedLink));
		}
		return query;
	}
	
	
	private Criteria criteriaByRelatedLink(String relatedLink) {
		return Criteria.where("relatedLinks").is(relatedLink);
	}
	
	private Criteria criteriaByEntity(String entity) {
		return new Criteria("entities").elemMatch(new Criteria("normal").regex(String.format(".*%s.*", entity), "i"));
	}
	
	private Criteria criteriaByLowerText(String text) {
		return new Criteria("text_lower").regex(String.format(".*%s.*", text), "i");
	}
	
	private Criteria criteriaByCreationDate(final Date creationDate) {
		final Date initDateInterval = PostDAO.getInitDateInterval(creationDate);
		final Date endDateInterval = PostDAO.getEndDateInterval(creationDate);
		return new Criteria("createdAt").gt(initDateInterval).lt(endDateInterval);
		
	}
}
