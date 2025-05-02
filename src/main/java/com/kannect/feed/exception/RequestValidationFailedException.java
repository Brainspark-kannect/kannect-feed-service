package com.kannect.feed.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class RequestValidationFailedException extends Exception {
	private static final long serialVersionUID = 1L;

	public RequestValidationFailedException() {
		super();
	}

	public RequestValidationFailedException(String message) {
		super(message);
	}
}
