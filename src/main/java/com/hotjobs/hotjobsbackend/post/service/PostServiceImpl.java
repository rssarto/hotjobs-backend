package com.hotjobs.hotjobsbackend.post.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotjobs.hotjobsbackend.post.dao.PostDAO;
import com.hotjobs.hotjobsbackend.post.domain.PaginatedList;
import com.hotjobs.hotjobsbackend.post.domain.Post;

@Service
public class PostServiceImpl implements PostService {
	
	private PostDAO postDAO;
	
	public PostServiceImpl(@Autowired PostDAO postRepository) {
		super();
		this.postDAO = postRepository;
	}
	
	@Override
	public PaginatedList<Post> listAll(int page, int pageSize) {
		return this.postDAO.listAll(page, pageSize);
	}

	@Override
	public PaginatedList<Post> findByEntity(String entity, int page, int pageSize) {
		return this.postDAO.findByEntity(entity, page, pageSize);
	}

	@Override
	public PaginatedList<Post> findByCreationDate(Date creationDate, int page, int pageSize) {
		return this.postDAO.findByCreationDate(creationDate, page, pageSize);
	}

	@Override
	public long total() {
		return this.postDAO.total();
	}

	@Override
	public long countByEntity(String entity) {
		return this.postDAO.countByEntity(entity);
	}
	
	@Override
	public PaginatedList<Post> findByRelatedLink(String relatedLink, final int page, final int pageSize){
		return this.postDAO.findByRelatedLink(relatedLink, page, pageSize);
	}
	
	@Override
	public PaginatedList<Post> findByCreationDateAndEntity(Date creationDate, String entity, int page, int pageSize){
		return this.postDAO.findByCreationDateAndEntity(creationDate, entity, page, pageSize);
	}

	@Override
	public PaginatedList<Post> findByText(String filter, int pageIndex, int pageSize) {
		return this.postDAO.findByText(filter.toLowerCase(), pageIndex, pageSize);
	}
	
}
