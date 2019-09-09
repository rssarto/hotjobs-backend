package com.hotjobs.hotjobsbackend.exception;

@SuppressWarnings("serial")
public class DuplicatedTextException extends RuntimeException {
	
	private String message;
	
	public DuplicatedTextException(final String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
