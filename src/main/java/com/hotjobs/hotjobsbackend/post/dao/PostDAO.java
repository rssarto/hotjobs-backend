package com.hotjobs.hotjobsbackend.post.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.hotjobs.hotjobsbackend.post.domain.PaginatedList;
import com.hotjobs.hotjobsbackend.post.domain.Post;

public interface PostDAO {
	
	PaginatedList<Post> listAll(int page, int pageSize);
	PaginatedList<Post> findByEntity(String entity, int page, int pageSize);
	PaginatedList<Post> findByCreationDate(Date creationDate, int page, int pageSize);
	PaginatedList<Post> findByRelatedLink(String relatedLink, int page, int pageSize);
	PaginatedList<Post> findByCreationDateAndEntity(Date creationDate, String entity, int page, int pageSize);
	PaginatedList<Post> findByText(String text, int page, int pageSize);
	PaginatedList<Post> findByTextAndEntityAndCreationDate(String text, String entity, Date creationDate, int page,int pageSize);
	long total();
	long countByEntity(String entity);
	long countByCreationDate(Date creationDate);
	long countByCreationDateAndEntity(Date creationDate, String entity);
	long countByTextAndEntityAndCreationDate(String text, String entity, Date creationDate);
	
	public static Pageable getPageable(int page, int pageSize) {
		return PageRequest.of(page - 1, pageSize, Direction.DESC, "createdAt");
	}
	
	public static Date getInitDateInterval(Date creationDate) {
		Date iniDate = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Post.STR_DATE_FORMAT);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(creationDate);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		try {
			iniDate = simpleDateFormat.parse(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "T23:59:59");
		} catch (ParseException e) {
		}
		return iniDate;
	}
	
	public static Query createQueryByTextAndEntityAndCreationDate(final String text, final String entity, final Date creationDate) {
		return new Query().addCriteria(criteriaByLowerText(text))
				          .addCriteria(criteriaByEntity(entity))
				          .addCriteria(criteriaByCreationDate(creationDate));
	}
	
	public static Query createQueryCountByRelatedLink(String relatedLink) {
		return new Query(relatedLinkCriteria(relatedLink));
	}

	public static Criteria relatedLinkCriteria(String relatedLink) {
		return Criteria.where("relatedLinks").is(relatedLink);
	}
	
	public static Query createRegexQueryByEntity(String entity) {
		final Query query = new Query(criteriaByEntity(entity));
		return query;
	}

	public static Criteria criteriaByEntity(String entity) {
		return new Criteria("entities").elemMatch(new Criteria("normal").regex(String.format(".*%s.*", entity), "i"));
	}
	
	public static Criteria criteriaByLowerText(String text) {
		return new Criteria("text_lower").regex(String.format(".*%s.*", text), "i");
	}
	
	public static Criteria criteriaByCreationDate(final Date creationDate) {
		final Date initDateInterval = PostDAO.getInitDateInterval(creationDate);
		final Date endDateInterval = PostDAO.getEndDateInterval(creationDate);
		return new Criteria("createdAt").gt(initDateInterval).lt(endDateInterval);
		
	}
	
	public static Date getEndDateInterval(Date creationDate) {
		Date endDate = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Post.STR_DATE_FORMAT);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(creationDate);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		try {
			endDate = simpleDateFormat.parse(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "T00:00:00");
		} catch (ParseException e) {
		}
		return endDate;
	}
}
