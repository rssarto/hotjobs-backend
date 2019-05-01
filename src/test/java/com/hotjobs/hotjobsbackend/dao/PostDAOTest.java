package com.hotjobs.hotjobsbackend.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.hotjobs.hotjobsbackend.post.dao.PostDAO;
import com.hotjobs.hotjobsbackend.post.dao.PostDAOImpl;
import com.hotjobs.hotjobsbackend.post.domain.PaginatedList;
import com.hotjobs.hotjobsbackend.post.domain.Post;
import com.hotjobs.hotjobsbackend.post.repository.PostRepository;
import com.hotjobs.hotjobsbackend.repository.PostRepositoryTest;

@RunWith(SpringRunner.class)
@DataMongoTest
public class PostDAOTest {
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	private PostDAO postDao;
	
	@Before
	public void before() {
		this.postDao = new PostDAOImpl(this.postRepository, this.mongoTemplate);
	}
	
	@Test
	public void countPosts_ReturnTotalPosts() {
		final List<Post> posts = PostRepositoryTest.getPosts();
		this.postRepository.deleteAll();
		this.postRepository.insert(posts);
		long total = this.postDao.total();
		assertThat(total).isEqualTo(posts.size());
	}
	
	@Test
	public void countByEntity_ReturnCount() {
		final List<Post> posts = PostRepositoryTest.getPosts();
		this.postRepository.deleteAll();
		this.postRepository.insert(posts);
		long countByEntity = this.postDao.countByEntity("london");
		assertThat(countByEntity).isEqualTo(1L);
	}
	
	@Test
	public void findByRelatedLink_ReturnList() {
		final List<Post> posts = PostRepositoryTest.getPosts();
		this.postRepository.deleteAll();
		this.postRepository.insert(posts);
		final String relatedLink = "https://t.co/EbXMHk5Xiz";
		PaginatedList<Post> findByRelatedLink = this.postDao.findByRelatedLink(relatedLink, 1, 5);
		assertThat(findByRelatedLink).isNotNull();
		assertThat(findByRelatedLink.getPageIndex()).isEqualTo(1L);
		assertThat(findByRelatedLink.getPageNumber()).isEqualTo(5L);
		assertThat(findByRelatedLink.getResults()).isNotNull();
		assertThat(findByRelatedLink.getResults().size()).isEqualTo(1L);
		findByRelatedLink.getResults().forEach(postItem -> {
			assertThat(postItem.getRelatedLinks().contains(relatedLink)).isTrue();
		});
	}
	
	@Test
	public void findByCreationDateAndEntity_ShouldReturnList() {
		final List<Post> posts = PostRepositoryTest.getPosts();
		this.postRepository.deleteAll();
		this.postRepository.insert(posts);
		PaginatedList<Post> findByCreationDateAndEntity = this.postDao.findByCreationDateAndEntity(new Date(), "london", 1, 5);
		assertThat(findByCreationDateAndEntity).isNotNull();
		assertThat(findByCreationDateAndEntity.getTotal()).isEqualTo(1L);
		assertThat(findByCreationDateAndEntity.getResults()).isNotNull();
		assertThat(findByCreationDateAndEntity.getResults().size()).isEqualTo(1);
		findByCreationDateAndEntity.getResults().forEach(post -> {
			assertThat(posts.contains(post));
		});
	}

}
