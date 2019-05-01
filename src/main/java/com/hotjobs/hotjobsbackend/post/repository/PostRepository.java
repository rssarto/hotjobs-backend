package com.hotjobs.hotjobsbackend.post.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.hotjobs.hotjobsbackend.post.domain.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String>, QuerydslPredicateExecutor<Post>  {
	
	public static final String QUERY_REGEX_BY_ENTITY = "{entities: {$elemMatch: { normal: {$regex:'.*?0.*', $options:'i'}}}}";
	@Query(QUERY_REGEX_BY_ENTITY)
	List<Post> findByEntity(String entity, Pageable pageable);
	
	/**
	 * Search the posts created between two dates.
	 * @param init
	 * @param end
	 * @return List<Post> 
	 */
	List<Post> findByCreatedAtBetween(Date init, Date end, Pageable pageable);
}
