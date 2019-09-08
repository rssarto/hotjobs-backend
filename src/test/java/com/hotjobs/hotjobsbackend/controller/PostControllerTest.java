package com.hotjobs.hotjobsbackend.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotjobs.hotjobsbackend.post.controller.PostController;
import com.hotjobs.hotjobsbackend.post.domain.PaginatedList;
import com.hotjobs.hotjobsbackend.post.domain.Post;
import com.hotjobs.hotjobsbackend.post.domain.PostEntity;
import com.hotjobs.hotjobsbackend.post.repository.PostRepository;
import com.hotjobs.hotjobsbackend.post.service.PostService;
import com.hotjobs.hotjobsbackend.post.validator.PostValidator;
import com.hotjobs.hotjobsbackend.service.PostServiceTest;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers= {PostValidator.class, PostController.class})
public class PostControllerTest {
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@MockBean
	PostRepository postRepository;
	
	@MockBean
	PostService postService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void checkInstances() {
		assertThat(this.postService).isNotNull();
		assertThat(this.mockMvc).isNotNull();
	}
	
	@Test
	public void findByEntity_ReturnPostList() throws Exception {
		final String entity = "london";
		final int pageIndex = 1;
		final int pageSize = 5;
		final List<Post> posts = PostServiceTest.getPostList();
		final PaginatedList<Post> paginatedList = new PaginatedList<>(posts, pageIndex, pageSize, posts.size()); 
		
		when(this.postService.findByEntity(entity, pageIndex, pageSize)).then(answer -> {
			return paginatedList;
		});
		
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/api/v1/post/" + entity + "/" + pageIndex + "/" + pageSize))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.results.length()").value(posts.size()));
	}
	
	@Test
	public void countByEntity_ReturnTotal() throws Exception{
		final String entity = "london";
		final long quantity = 1L;
		when(this.postService.countByEntity(entity)).then(answer -> {
			return quantity;
		});
		
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/api/v1/post/" + entity + "/count"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(result -> {
				String contentAsString = result.getResponse().getContentAsString();
				assertThat(contentAsString).isNotNull();
				assertThat(StringUtils.isNotBlank(contentAsString)).isTrue();
				assertThat(Long.valueOf(contentAsString)).isEqualTo(quantity);
			});
	}
	
	@Test
	public void getAllPosts() throws Exception {
		final int pageIndex = 1;
		final int pageSize = 5;
		final List<Post> posts = PostServiceTest.getPostList();
		final PaginatedList<Post> paginatedList = new PaginatedList<>(posts, pageIndex, pageSize, posts.size());
		
		when(this.postService.listAll(pageIndex, pageSize)).then(answer -> {
			return paginatedList;
		});
		
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/api/v1/post/" + pageIndex + "/" + pageSize))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.total").value(paginatedList.getTotal()))
			.andExpect(jsonPath("$.pageIndex").value(pageIndex))
			.andExpect(jsonPath("$.pageNumber").value(pageSize));
	}
	
	@Test
	public void findByCreatedDate_ShouldRetornList() throws Exception {
		final int pageIndex = 1;
		final int pageSize = 5;
		final List<Post> posts = PostServiceTest.getPostList();
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		
		final String strCreationDate = (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.YEAR); 
		final PaginatedList<Post> paginatedList = new PaginatedList<>(posts, pageIndex, pageSize, posts.size());
		
		when(this.postService.findByCreationDate(ArgumentMatchers.any(Date.class), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).then(answer -> {
			return paginatedList;
		});
		
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/api/v1/post/creation/" + strCreationDate + "/" + pageIndex + "/" + pageSize))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.total").value(paginatedList.getTotal()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.pageIndex").value(pageIndex))
			.andExpect(MockMvcResultMatchers.jsonPath("$.pageNumber").value(pageSize));
	}
	
	@Test
	public void findByCreationDateAndEntity_ShouldReturnList() throws Exception {
		final int pageIndex = 1;
		final int pageSize = 5;
		final Calendar calendar = Calendar.getInstance();
		final String entity = "london";
		
		calendar.setTime(new Date());
		
		final String strCreationDate = (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.YEAR); 
		final List<Post> posts = Arrays.asList(new Post("Senior Java #Developer - FinTech - £110,000 - onezeero. ( City of London, UK )  - [ 📋 More Info… https://t.co/EbXMHk5Xiz", calendar.getTime(), Arrays.asList(new PostEntity("London", entity)), Arrays.asList("https://t.co/EbXMHk5Xiz")));
		final PaginatedList<Post> paginatedList = new PaginatedList<>(posts, pageIndex, pageSize, posts.size());
		
		when(this.postService.findByCreationDateAndEntity(ArgumentMatchers.any(Date.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).then(answer -> {
			return paginatedList;
		});
		
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/api/v1/post/creation/" + strCreationDate + "/" + entity + "/" + pageIndex + "/" + pageSize))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.total").value(paginatedList.getTotal()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.pageIndex").value(pageIndex))
			.andExpect(MockMvcResultMatchers.jsonPath("$.pageNumber").value(pageSize));
	}
	
	@Test
	public void findByText_ShouldRetornList() throws Exception {
		final int pageIndex = 1;
		final int pageSize = 5;
		final List<Post> posts = Arrays.asList(new Post("Senior Java #Developer - FinTech - £110,000 - onezeero. ( City of London, UK )  - [ 📋 More Info… https://t.co/EbXMHk5Xiz", new Date(), Arrays.asList(new PostEntity("London", "london")), Arrays.asList("https://t.co/EbXMHk5Xiz")));
		final PaginatedList<Post> paginatedList = new PaginatedList<>(posts, pageIndex, pageSize, posts.size());

		final String filter = "fintech";
		when(this.postService.findByText(filter, pageIndex, pageSize)).then(answer -> {
			return paginatedList;
		});
		
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/api/v1/post/text/" + filter + "/" + pageIndex + "/" + pageSize))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.total").value(paginatedList.getTotal()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.pageIndex").value(pageIndex))
			.andExpect(MockMvcResultMatchers.jsonPath("$.pageNumber").value(pageSize));
		
	}
	
	@Test
	public void createPost_shouldCreatePost() throws Exception {
		final Post post = new Post();
		post.setText("Senior Java #Developer - FinTech - £110,000 - onezeero. ( City of London, UK )  - [ 📋 More Info… https://t.co/EbXMHk5Xiz");
		
		when(this.postService.save(post)).then( answer -> {
			return post;
		});		
		
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/api/v1/post")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(post)))
			.andExpect(MockMvcResultMatchers.status().isCreated());
	}
	
	@Test
	public void createPost_ShouldNotCreatedWithBlankText() throws Exception {
		final Post post = new Post();
		
		when(this.postService.save(post)).then( answer -> {
			return post;
		});		
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/api/v1/post")
					.contentType(MediaType.APPLICATION_JSON)
					.content(mapper.writeValueAsString(post)))
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
		.andExpect(jsonPath("$.message").value("Validation error"))
		.andExpect(jsonPath("$.subErrors", hasSize(1)))
		.andExpect(jsonPath("$.subErrors[0].object").value("post"))
		.andExpect(jsonPath("$.subErrors[0].field").value("text"))
		.andExpect(jsonPath("$.subErrors[0].rejectedValue").value(IsNull.nullValue()))
		.andExpect(jsonPath("$.subErrors[0].message").value(Post.Messages.Fields.TEXT_MANDATORY));
		
	}
	
	@Test
	public void createPost_ShouldNotCreateWithDuplicatedLink() throws Exception {
		final Post post = new Post("Senior Java #Developer - FinTech - £110,000 - onezeero. ( City of London, UK )  - [ 📋 More Info… https://t.co/EbXMHk5Xiz", new Date(), Arrays.asList(new PostEntity("London", "london")), Arrays.asList("https://t.co/EbXMHk5Xiz"));
		final List<Post> posts = new ArrayList<>();
		posts.add(post);
		
		when(this.postService.findByRelatedLink(post.getRelatedLinks().get(0), 1, 1)).then(answer -> {
			return new PaginatedList<Post>(posts, 1, 1, 10);
		});		
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/api/v1/post")
					.contentType(MediaType.APPLICATION_JSON)
					.content(mapper.writeValueAsString(post)))
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
		.andExpect(jsonPath("$.message").value(String.format("Duplicated link(s) %s", post.getRelatedLinks().toString())));		
	}
}
