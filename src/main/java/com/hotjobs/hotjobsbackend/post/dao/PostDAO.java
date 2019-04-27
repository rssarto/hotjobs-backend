package com.hotjobs.hotjobsbackend.post.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import com.hotjobs.hotjobsbackend.post.domain.PaginatedList;
import com.hotjobs.hotjobsbackend.post.domain.Post;

public interface PostDAO {
	
	PaginatedList<Post> listAll(int page, int pageSize);
	PaginatedList<Post> findByEntity(String entity, int page, int pageSize);
	PaginatedList<Post> findByCreationDate(Date creationDate, int page, int pageSize);
	long total();
	long countByEntity(String entity);
	long countByCreationDate(Date creationDate);
	
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
	PaginatedList<Post> findByRelatedLink(String relatedLink, int page, int pageSize);
}
