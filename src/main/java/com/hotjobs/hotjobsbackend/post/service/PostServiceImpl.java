package com.hotjobs.hotjobsbackend.post.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.hotjobs.hotjobsbackend.post.dao.PostDAO;
import com.hotjobs.hotjobsbackend.post.domain.PaginatedList;
import com.hotjobs.hotjobsbackend.post.domain.Post;
import com.hotjobs.hotjobsbackend.post.properties.PostPropertySource;

@Service
public class PostServiceImpl implements PostService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PostServiceImpl.class);
	
	private final PostDAO postDAO;
	private final PostPropertySource postPropertySource;
	
	public PostServiceImpl(@Autowired PostDAO postRepository, PostPropertySource postPropertySource) {
		super();
		this.postDAO = postRepository;
		this.postPropertySource = postPropertySource;
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

	@Override
	public PaginatedList<Post> findByTextAndEntityAndCreationDate(String text, String entity, Date creationDate,int page, int pageSize) {
		return this.postDAO.findByTextAndEntityAndCreationDate(text.toLowerCase(), entity, creationDate, page, pageSize);
	}
	
	@Scheduled(cron="0 0 23 * * * ")
//	@Scheduled(cron="0 * * * * ?") every minute
	private void removeOldPost() {
		LOGGER.info("initiating removeOldPost");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, -this.postPropertySource.getDaysToKeep());
		List<Post> postsToRemove = this.postDAO.findByCreatedAtLessThan(calendar.getTime());
		if( Objects.nonNull(postsToRemove) && !postsToRemove.isEmpty() ) {
			postsToRemove.forEach(post -> {
				LOGGER.info("removing...");
				LOGGER.info(post.toString());
				this.postDAO.deleteById(post.get_id());
			});
		}
	}

	@Override
	public Post save(final Post post) {
		return this.postDAO.save(post);
	}
}
