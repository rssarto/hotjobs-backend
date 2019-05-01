package com.hotjobs.hotjobsbackend.post.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotjobs.hotjobsbackend.post.domain.PaginatedList;
import com.hotjobs.hotjobsbackend.post.domain.Post;
import com.hotjobs.hotjobsbackend.post.service.PostService;

@RestController
@RequestMapping(value="/api/v1/post")
public class PostController {
	
	private PostService postService;
	
	public PostController(@Autowired PostService postService) {
		this.postService = postService;
	}
	
	@GetMapping("/{page}/{pageSize}")
	public ResponseEntity<PaginatedList<Post>> listAll(@PathVariable final int page,@PathVariable final int pageSize){
		PaginatedList<Post> listAll = this.postService.listAll(page, pageSize);
		return new ResponseEntity<>(listAll, HttpStatus.OK);
	}
	
	@GetMapping("/{entity}/{page}/{pageSize}")
	public ResponseEntity<PaginatedList<Post>> findByEntity(@PathVariable String entity, @PathVariable final int page, @PathVariable final int pageSize){
		PaginatedList<Post> findByEntity = this.postService.findByEntity(entity, page, pageSize);
		return new ResponseEntity<>(findByEntity, HttpStatus.OK);
	}
	
	@GetMapping("/relatedLink/{page}/{pageSize}")
	public ResponseEntity<PaginatedList<Post>> findByRelatedLink(@RequestParam(name="link") final String relatedLink, @PathVariable final int page, @PathVariable final int pageSize){
		PaginatedList<Post> findByRelatedLink = this.postService.findByRelatedLink(relatedLink, page, pageSize);
		return new ResponseEntity<>(findByRelatedLink, HttpStatus.OK);
	}
	
	@GetMapping("/creation/{creationDate}/{page}/{pageSize}")
	public ResponseEntity<PaginatedList<Post>> findByCreationDate(@PathVariable Date creationDate, @PathVariable int page, @PathVariable int pageSize){
		final PaginatedList<Post> findByCreationDate = this.postService.findByCreationDate(creationDate, page, pageSize);
		return new ResponseEntity<>(findByCreationDate, HttpStatus.OK);
	}
	
	@GetMapping("/creation/{creationDate}/{entity}/{page}/{pageSize}")
	public ResponseEntity<PaginatedList<Post>> findByCreationDate(@PathVariable Date creationDate, @PathVariable String entity, @PathVariable int page, @PathVariable int pageSize){
		final PaginatedList<Post> resultList = this.postService.findByCreationDateAndEntity(creationDate, entity, page, pageSize);
		return new ResponseEntity<>(resultList, HttpStatus.OK);
	}
	
	@GetMapping("/{entity}/count")
	public ResponseEntity<Long> countByEntity(@PathVariable final String entity){
		long countByEntity = this.postService.countByEntity(entity);
		return new ResponseEntity<>(countByEntity, HttpStatus.OK);
	}

}
