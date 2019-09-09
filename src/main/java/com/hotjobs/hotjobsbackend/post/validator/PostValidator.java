package com.hotjobs.hotjobsbackend.post.validator;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.hotjobs.hotjobsbackend.exception.DuplicatedLinkException;
import com.hotjobs.hotjobsbackend.exception.DuplicatedTextException;
import com.hotjobs.hotjobsbackend.post.domain.PaginatedList;
import com.hotjobs.hotjobsbackend.post.domain.Post;
import com.hotjobs.hotjobsbackend.post.service.PostService;

@Component
public class PostValidator implements Validator {
	
	private PostService postService;
	
	@Autowired
	public PostValidator(final PostService postService) {
		this.postService = postService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Post.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		final Post post = (Post) obj;
		
		final PaginatedList<Post> findByText = this.postService.findByText(post.getText(), 1, 1);
		if( Objects.nonNull(findByText) && findByText.getTotal() > 0 ) {
			throw new DuplicatedTextException(String.format("Duplicated text: %s", post.getText()));
		}
		
		if( Objects.nonNull(post.getRelatedLinks()) && !post.getRelatedLinks().isEmpty() ) {
			final long duplicateLinksCount = post.getRelatedLinks().stream().mapToLong(link -> this.postService.findByRelatedLink(link, 1, 1).getTotal()).sum();
			if (duplicateLinksCount > 0) {
				throw new DuplicatedLinkException(String.format("Duplicated link(s) %s", post.getRelatedLinks().toString()));
			}
		}
	}
}
