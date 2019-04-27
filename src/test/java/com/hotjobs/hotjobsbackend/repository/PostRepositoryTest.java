package com.hotjobs.hotjobsbackend.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.hotjobs.hotjobsbackend.config.mongodb.SystemProfileValueSource2;
import com.hotjobs.hotjobsbackend.post.dao.PostDAO;
import com.hotjobs.hotjobsbackend.post.domain.Post;
import com.hotjobs.hotjobsbackend.post.domain.PostEntity;
import com.hotjobs.hotjobsbackend.post.repository.PostRepository;

@ProfileValueSourceConfiguration(value = SystemProfileValueSource2.class)
@IfProfileValue(name = ACTIVE_PROFILES_PROPERTY_NAME, value = "test") //"spring.profiles.active"
@RunWith(SpringRunner.class)
@DataMongoTest()
public class PostRepositoryTest {
	
	@Autowired
	private PostRepository postRepository;
	
	@Test
	public void testInstances() {
		assertThat(this.postRepository).isNotNull();
	}
	
	@Test
	public void findPostByEntity_ReturnsPostList() {
		final List<Post> posts = new ArrayList<>();
		final Post searchPost = new Post("Senior Java #Developer - FinTech - £110,000 - onezeero. ( City of London, UK )  - [ 📋 More Info… https://t.co/EbXMHk5Xiz", new Date(), Arrays.asList(new PostEntity("London", "london")), Arrays.asList("https://t.co/EbXMHk5Xiz"));
		
		this.postRepository.deleteAll();
		
		posts.add(searchPost);
		posts.add(new Post("TechNet IT Recruitment (Permanent): Java Developer - Paris ... https://t.co/q5hOjvtU8l", new Date(), Arrays.asList(new PostEntity("- Paris", "paris")), Arrays.asList("https://t.co/q5hOjvtU8l")));
		
		this.postRepository.insert(posts);
		
		List<Post> findByEntityList = this.postRepository.findByEntity("london", getPageRequest());
		assertThat(findByEntityList).isNotNull();
		assertThat(findByEntityList.isEmpty()).isFalse();
		assertThat(findByEntityList.size() == 1).isTrue();
		assertThat(searchPost.equals(findByEntityList.get(0))).isTrue();
	}
	
	@Test
	public void findPostByPeriod_ReturnPostList() throws ParseException {
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Post.STR_DATE_FORMAT);
		final List<Post> posts = new ArrayList<>();
		Date now = new Date();
		this.postRepository.deleteAll();
		
		Calendar instance = Calendar.getInstance();
		instance.setTime(now);
		instance.add(Calendar.DATE, -1);
		Date iniDate = simpleDateFormat.parse(instance.get(Calendar.YEAR) + "-" + (instance.get(Calendar.MONTH) + 1) + "-" + instance.get(Calendar.DAY_OF_MONTH) + "T23:59:59");
		
		instance.setTime(now);
		instance.add(Calendar.DATE, 1);
		Date endDate = simpleDateFormat.parse(instance.get(Calendar.YEAR) + "-" + (instance.get(Calendar.MONTH) + 1) + "-" + instance.get(Calendar.DAY_OF_MONTH) + "T00:00:00");
		final Post searchPost = new Post("initSenior Java #Developer - FinTech - £110,000 - onezeero. ( City of London, UK )  - [ 📋 More Info… https://t.co/EbXMHk5Xiz", now, Arrays.asList(new PostEntity("London", "london")), Arrays.asList("https://t.co/EbXMHk5Xiz"));
		posts.add(searchPost);
		posts.add(new Post("endTechNet IT Recruitment (Permanent): Java Developer - Paris ... https://t.co/q5hOjvtU8l", now, Arrays.asList(new PostEntity("- Paris", "paris")), Arrays.asList("https://t.co/q5hOjvtU8l")));
		this.postRepository.insert(posts);
		
		List<Post> findByPeriod = this.postRepository.findByCreatedAtBetween(iniDate, endDate, getPageRequest());
		assertThat(findByPeriod).isNotNull();
		assertThat(findByPeriod.size() == posts.size()).isTrue();
		for( Post post : posts ) {
			assertThat(findByPeriod.contains(post)).isTrue();
		}
	}
	
	@Test
	public void listAll_ReturnPostList() {
		final List<Post> posts = PostRepositoryTest.getPosts();
		this.postRepository.deleteAll();
		this.postRepository.insert(posts);
		
		Page<Post> postPage = this.postRepository.findAll(PostDAO.getPageable(1, 5));
		assertThat(postPage).isNotNull();
		assertThat(postPage.getTotalElements()).isEqualTo(posts.size());
	}
	
	public static Pageable getPageRequest() {
        final int page = 0;
        final int pageSize = 5;
        return PostDAO.getPageable(page, pageSize);
	}
	
	public static List<Post> getPosts(){
		final List<Post> posts = new ArrayList<>();
		Date now = new Date();
		
		final Post searchPost = new Post("initSenior Java #Developer - FinTech - £110,000 - onezeero. ( City of London, UK )  - [ 📋 More Info… https://t.co/EbXMHk5Xiz", now, Arrays.asList(new PostEntity("London", "london")), Arrays.asList("https://t.co/EbXMHk5Xiz"));
		posts.add(searchPost);
		posts.add(new Post("endTechNet IT Recruitment (Permanent): Java Developer - Paris ... https://t.co/q5hOjvtU8l", now, Arrays.asList(new PostEntity("- Paris", "paris")), Arrays.asList("https://t.co/q5hOjvtU8l")));
		return posts;
	}
	
	 

}
