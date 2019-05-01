package com.hotjobs.hotjobsbackend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.hotjobs.hotjobsbackend.post.dao.PostDAO;
import com.hotjobs.hotjobsbackend.post.domain.PaginatedList;
import com.hotjobs.hotjobsbackend.post.domain.Post;
import com.hotjobs.hotjobsbackend.post.domain.PostEntity;
import com.hotjobs.hotjobsbackend.post.service.PostService;
import com.hotjobs.hotjobsbackend.post.service.PostServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class PostServiceTest {
	
	@Mock
	PostDAO postDao;
	
	private PostService postService;
	
	@Before
	public void before() {
		this.postService = new PostServiceImpl(this.postDao);
	}
	
	@Test
	public void findByEntity_ReturnPostList() {
		final int page = 0, pageSize = 5;
		final String entity = "london";
		final List<Post> posts = getPostList();
		final PaginatedList<Post> paginatedList = new PaginatedList<>(posts, page, pageSize, posts.size());
		
		when(this.postDao.findByEntity(entity, page, pageSize)).then(answer -> {
			return paginatedList;
		});
		
		PaginatedList<Post> findByEntity = this.postService.findByEntity(entity, page, pageSize);
		assertThat(findByEntity).isNotNull();
		assertThat(findByEntity.getResults().size() == posts.size()).isTrue();
		for(Post post: posts) {
			assertThat(findByEntity.getResults().contains(post)).isTrue();
		}
	}
	
	@Test
	public void findByDate_ReturnPostList() {
		final int page = 0, pageSize = 5;
		final Date creationDate = new Date();
		final List<Post> posts = getPostList();
		final PaginatedList<Post> paginatedList = new PaginatedList<>(posts, page, pageSize, posts.size());
		
		when(this.postDao.findByCreationDate(creationDate,page, pageSize)).then(answer -> {
			return paginatedList;
		});
		
		PaginatedList<Post> findByEntity = this.postService.findByCreationDate(creationDate, page, pageSize);
		assertThat(findByEntity).isNotNull();
		assertThat(findByEntity.getResults().size() == posts.size()).isTrue();
		for(Post post: posts) {
			assertThat(findByEntity.getResults().contains(post)).isTrue();
		}
	}
	
	@Test
	public void getTotal_ReturnTotalPosts() {
		final long total = 2;
		
		when( this.postDao.total() ).then(answer -> {
			return total;
		});
		
		long count = this.postService.total();
		assertThat(count == total).isTrue();
	}
	
	@Test
	public void shouldFindByCreationDateAndEntity_ShouldReturnList() {
		final String entity = "london";
		final Date creationDate = new Date();
		final int page = 1;
		final int pageSize = 5;
		final List<Post> posts = Arrays.asList(new Post("Senior Java #Developer - FinTech - £110,000 - onezeero. ( City of London, UK )  - [ 📋 More Info… https://t.co/EbXMHk5Xiz", creationDate, Arrays.asList(new PostEntity("London", entity)), Arrays.asList("https://t.co/EbXMHk5Xiz")));
		final PaginatedList<Post> paginatedList = new PaginatedList<>(posts, page, pageSize, posts.size());
		when(this.postDao.findByCreationDateAndEntity(creationDate, entity, page, pageSize)).then(answer -> {
			return paginatedList;
		});
		
		PaginatedList<Post> findByCreationDateAndEntity = this.postService.findByCreationDateAndEntity(creationDate, entity, page, pageSize);
		assertThat(findByCreationDateAndEntity).isNotNull();
		assertThat(findByCreationDateAndEntity.getPageIndex()).isEqualTo(page);
		assertThat(findByCreationDateAndEntity.getPageNumber()).isEqualTo(pageSize);
		assertThat(findByCreationDateAndEntity.getTotal()).isEqualTo(posts.size());
		assertThat(findByCreationDateAndEntity.getResults()).isNotNull();
		findByCreationDateAndEntity.getResults().forEach(post -> {
			assertThat(posts.contains(post)).isTrue();
		});
	}
	
	public static List<Post> getPostList(){
		final List<Post> posts = new ArrayList<>();
		posts.add(new Post("Senior Java #Developer - FinTech - £110,000 - onezeero. ( City of London, UK )  - [ 📋 More Info… https://t.co/EbXMHk5Xiz", new Date(), Arrays.asList(new PostEntity("London", "london")), Arrays.asList("https://t.co/EbXMHk5Xiz")));
		posts.add(new Post("TechNet IT Recruitment (Permanent): Java Developer - Paris ... https://t.co/q5hOjvtU8l", new Date(), Arrays.asList(new PostEntity("- Paris", "paris")), Arrays.asList("https://t.co/q5hOjvtU8l")));
		return posts;
	}

}
