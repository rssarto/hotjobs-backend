package com.hotjobs.hotjobsbackend.post.service;

import java.util.Date;

import com.hotjobs.hotjobsbackend.post.domain.PaginatedList;
import com.hotjobs.hotjobsbackend.post.domain.Post;

public interface PostService {
	PaginatedList<Post> listAll(int page, int pageSize);
	PaginatedList<Post> findByEntity(String entity, int page, int pageSize);
	PaginatedList<Post> findByCreationDate(Date creationDate, int page, int pageSize);
	PaginatedList<Post> findByRelatedLink(String relatedLink, int page, int pageSize);
	PaginatedList<Post> findByCreationDateAndEntity(Date creationDate, String entity, int page, int pageSize);
	PaginatedList<Post> findByText(String filter, int pageIndex, int pageSize);
	PaginatedList<Post> findByTextAndEntityAndCreationDate(String text, String entity, Date creationDate, int page,int pageSize);
	long total();
	long countByEntity(String entity);
}
