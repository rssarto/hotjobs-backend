package com.hotjobs.hotjobsbackend.exception;

@SuppressWarnings("serial")
public class DuplicatedLinkException extends RuntimeException {
	
	private String message;
	
	public DuplicatedLinkException(final String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
