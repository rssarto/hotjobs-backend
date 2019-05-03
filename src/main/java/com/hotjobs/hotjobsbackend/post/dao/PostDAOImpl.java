package com.hotjobs.hotjobsbackend.post.dao;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.hotjobs.hotjobsbackend.post.domain.PaginatedList;
import com.hotjobs.hotjobsbackend.post.domain.Post;
import com.hotjobs.hotjobsbackend.post.repository.PostRepository;

@Service
public class PostDAOImpl implements PostDAO {
	
	private PostRepository postRepository;
	private MongoTemplate mongoTemplate;
	
	public PostDAOImpl(@Autowired PostRepository postRepository, @Autowired MongoTemplate mongoTemplate) {
		this.postRepository = postRepository;
		this.mongoTemplate = mongoTemplate;
	}
	
	@Override
	public PaginatedList<Post> listAll(final int page, final int pageSize) {
		final List<Post> resultList = this.postRepository.findAll(PostDAO.getPageable(page, pageSize)).stream().collect(Collectors.toList());
		final long count = this.total();
		return new PaginatedList<>(resultList, page, pageSize, count);
	}

	@Override
	public PaginatedList<Post> findByEntity(final String entity, final int page, final int pageSize) {
		final List<Post> resultList = this.mongoTemplate.find(new PostQueryBuilder().setEntity(entity).build().with(PostDAO.getPageable(page, pageSize)), Post.class, Post.COLLECTION_NAME);
		final long countByEntity = this.countByEntity(entity);
		return new PaginatedList<>(resultList, page, pageSize, countByEntity);
	}

	@Override
	public PaginatedList<Post> findByCreationDate(final Date creationDate, final int page, final int pageSize) {
		final List<Post> findByCreatedAtBetween = this.postRepository.findByCreatedAtBetween(PostDAO.getInitDateInterval(creationDate), PostDAO.getEndDateInterval(creationDate), PostDAO.getPageable(page, pageSize));
		final long countByCreationDate = this.countByCreationDate(creationDate);
		return new PaginatedList<>(findByCreatedAtBetween, page, pageSize, countByCreationDate);
	}
	
	@Override
	public PaginatedList<Post> findByCreationDateAndEntity(final Date creationDate, final String entity, final int page, final int pageSize) {
		PostQueryBuilder postQueryBuilder = new PostQueryBuilder();
		postQueryBuilder.setCreationDate(creationDate)
						.setEntity(entity);
		List<Post> resultList = this.mongoTemplate.find(postQueryBuilder.build().with(PostDAO.getPageable(page, pageSize)), Post.class);
		final long countByCreationDateAndEntity = this.countByCreationDateAndEntity(creationDate, entity);
		return new PaginatedList<>(resultList, page, pageSize, countByCreationDateAndEntity);
	}
	
	@Override
	public PaginatedList<Post> findByTextAndEntityAndCreationDate(final String text, final String entity, final Date creationDate, final int page, final int pageSize){
		PostQueryBuilder postQueryBuilder = new PostQueryBuilder();
		postQueryBuilder.setTextLower(text)
						.setEntity(entity)
						.setCreationDate(creationDate);
		List<Post> resultList = this.mongoTemplate.find(postQueryBuilder.build().with(PostDAO.getPageable(page, pageSize)), Post.class);
		long countByTextAndEntityAndCreationDate = this.countByTextAndEntityAndCreationDate(text, entity, creationDate);
		return new PaginatedList<>(resultList, page, pageSize, countByTextAndEntityAndCreationDate);
	}
	
	@Override
	public long countByTextAndEntityAndCreationDate(final String text, final String entity, final Date creationDate) {
		PostQueryBuilder postQueryBuilder = new PostQueryBuilder();
		postQueryBuilder.setTextLower(text)
						.setEntity(entity)
						.setCreationDate(creationDate);		
		return this.mongoTemplate.count(postQueryBuilder.build(), Post.COLLECTION_NAME);
	}
	
	@Override	
	public long countByCreationDateAndEntity(final Date creationDate, final String entity) {
		PostQueryBuilder postQueryBuilder = new PostQueryBuilder();
		postQueryBuilder.setEntity(entity)
						.setCreationDate(creationDate);		
		return this.mongoTemplate.count(postQueryBuilder.build(), Post.COLLECTION_NAME);
	}
	
	@Override
	public PaginatedList<Post> findByRelatedLink(String relatedLink, final int page, final int pageSize){
		final Query relatedLinkQuery = new PostQueryBuilder().setRelatedLink(relatedLink).build();
		final List<Post> results = this.mongoTemplate.find(relatedLinkQuery.with(PostDAO.getPageable(page, pageSize)), Post.class, Post.COLLECTION_NAME);
		final long countByRelatedLink = this.mongoTemplate.count(relatedLinkQuery, Post.COLLECTION_NAME);
		return new PaginatedList<>(results, page, pageSize, countByRelatedLink);
	}

	@Override
	public long total() {
		return this.postRepository.count();		
	}

	@Override
	public long countByEntity(final String entity) {
		return this.mongoTemplate.count(new PostQueryBuilder().setEntity(entity).build(), Post.COLLECTION_NAME);
	}
	
	@Override
	public PaginatedList<Post> findByText(String text, int page, int pageSize){
		List<Post> postList = this.postRepository.findByText_lowerRegex(text, PostDAO.getPageable(page, pageSize));
		long count = this.postRepository.countByText_lowerRegex(text);
		return new PaginatedList<>(postList, page, pageSize, count);
	}
	
	@Override
	public long countByCreationDate(final Date creationDate) {
		return this.mongoTemplate.count(new PostQueryBuilder().setCreationDate(creationDate).build(), Post.COLLECTION_NAME);
	}
	
}
