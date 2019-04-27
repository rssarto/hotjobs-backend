package com.hotjobs.hotjobsbackend.post.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaginatedList<T> {
	
	private List<T> results;
	private long pageIndex;
	private long pageNumber;
	private long total;

}
