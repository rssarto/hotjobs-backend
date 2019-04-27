package com.hotjobs.hotjobsbackend.post.dao;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
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
		final List<Post> resultList = this.mongoTemplate.find(this.createRegexQueryByEntity(entity).with(PostDAO.getPageable(page, pageSize)), Post.class, Post.COLLECTION_NAME);
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
	public PaginatedList<Post> findByRelatedLink(String relatedLink, final int page, final int pageSize){
		final List<Post> results = this.mongoTemplate.find(this.createQueryCountByRelatedLink(relatedLink), Post.class, Post.COLLECTION_NAME);
		final long countByRelatedLink = this.mongoTemplate.count(this.createQueryCountByRelatedLink(relatedLink), Post.COLLECTION_NAME);
		return new PaginatedList<>(results, page, pageSize, countByRelatedLink);
	}

	@Override
	public long total() {
		return this.postRepository.count();		
	}

	@Override
	public long countByEntity(final String entity) {
		return this.mongoTemplate.count(this.createRegexQueryByEntity(entity), Post.COLLECTION_NAME);
	}
	
	private Query createQueryCountByRelatedLink(String relatedLink) {
		return new Query(Criteria.where("relatedLinks").is(relatedLink));
	}
	
	private Query createRegexQueryByEntity(String entity) {
		final Query query = new Query(new Criteria("entities").elemMatch(new Criteria("normal").regex(String.format(".*%s.*", entity), "i")));
		return query;
	}

	@Override
	public long countByCreationDate(final Date creationDate) {
		final Date initDateInterval = PostDAO.getInitDateInterval(creationDate);
		final Date endDateInterval = PostDAO.getEndDateInterval(creationDate);
		final Query query = new Query(new Criteria("createdAt").gt(initDateInterval).lt(endDateInterval));
		return this.mongoTemplate.count(query, Post.COLLECTION_NAME);
	}
}
